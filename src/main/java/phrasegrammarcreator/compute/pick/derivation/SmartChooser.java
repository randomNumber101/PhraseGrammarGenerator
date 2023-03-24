package phrasegrammarcreator.compute.pick.derivation;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
import phrasegrammarcreator.core.derive.possibilities.tree.ChoicePossibilities;
import phrasegrammarcreator.core.derive.possibilities.tree.Possibilities;
import phrasegrammarcreator.core.derive.possibilities.tree.PossibilityTreeAggregator;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.main.Randomizer;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SmartChooser extends DerivationChooser{

    /**
     *
     */
    public enum STRATEGY {
        NARROW_DOWN_LINEAR, BUILD_UP_SOFTMAX, NARROW_DOWN_SOFTMAX
    }

    private final int POSSIBILITY_CAP = 1000;
    private final int DEPTH_CAP = 6;

    private final double EPS = 0.01;

    private HashMap<Variable, ChoicePossibilities> variablePossibilities;

    private HashMap<Variable, Double> variableWeights;

    private HashMap<Rule, Double> ruleWeights;

    private STRATEGY strategy = STRATEGY.NARROW_DOWN_SOFTMAX;

    public SmartChooser(FormalGrammar grammar, List<Rule> rules) {
        super(rules);
        CfRuleContainer container = new CfRuleContainer(grammar);
        variablePossibilities = new HashMap<>();
        variableWeights = new HashMap<>();
        ruleWeights = new HashMap<>();

        for (Variable v : grammar.getVocabulary().getVariables()) {
            ChoicePossibilities cp = new ChoicePossibilities(container, v.createInstance());
            calculateTillCap(cp, POSSIBILITY_CAP, DEPTH_CAP);
            double weight = cp.accept(new EndPhrasePossibilityAggregator());

            variableWeights.put(v, weight);
            variablePossibilities.put(v, cp);
        }

        for (Rule<?, ?> r : rules) {
            double weightSum = r.getTarget().stream().map(vi -> variableWeights.get(vi.getBuilder()))
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getSum();
            ruleWeights.put(r, weightSum);
        }
    }

    private void calculateTillCap(Possibilities p, int possibiliyCap, int depthCap) {
        for (int i = 0; i < depthCap; i++) {
            if (p.getCount() > possibiliyCap)
                return;
            p.calculateNext();
        }
    }

    @Override
    public Derivation pick(DerivationSet derivations) {
        if (derivations == null || derivations.isEmpty())
            return null;

        double[] probabilities = new double[derivations.size()];
        List<Rule> orderedRules = derivations.stream().map(Derivation::getRule).toList();

        switch (strategy) {
            case NARROW_DOWN_LINEAR -> {
                double ruleSum = orderedRules.stream()
                        .map(r -> 1d / (EPS + ruleWeights.get(r)))
                        .collect(Collectors.summarizingDouble(Double::doubleValue))
                        .getSum();

                for (int i = 0; i < probabilities.length; i++) {
                    Rule r = orderedRules.get(i);
                    probabilities[i] =  1d / ((ruleWeights.get(r) + EPS) * ruleSum);
                }
            }
            case NARROW_DOWN_SOFTMAX -> {
                double ruleExpSum = orderedRules.stream()
                        .map(r -> Math.exp(-ruleWeights.get(r)))
                        .collect(Collectors.summarizingDouble(Double::doubleValue))
                        .getSum();

                for (int i = 0; i < probabilities.length; i++) {
                    Rule r = orderedRules.get(i);
                    probabilities[i] =  Math.exp(-ruleWeights.get(r)) / ruleExpSum;
                }
            }
            case BUILD_UP_SOFTMAX -> {
                double ruleExpSum = orderedRules.stream()
                        .map(r -> Math.exp(ruleWeights.get(r)))
                        .collect(Collectors.summarizingDouble(Double::doubleValue))
                        .getSum();

                for (int i = 0; i < probabilities.length; i++) {
                    Rule r = orderedRules.get(i);
                    probabilities[i] =  Math.exp(ruleWeights.get(r)) / ruleExpSum;
                }
            }
        }

        Rule<?, ?> picked = orderedRules.get(Randomizer.getInstance().sample(probabilities));
        for (Derivation d : derivations) {
            if (d.getRule() == picked)
                return d;
        }
        return null;
    }

    public STRATEGY getStrategy() {
        return strategy;
    }

    public void setStrategy(STRATEGY strategy) {
        this.strategy = strategy;
    }



    private static class EndPhrasePossibilityAggregator extends PossibilityTreeAggregator<Double, Double> {

        @Override
        public Double product(List<Double> in) {
            return in.stream()
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getSum();
        }

        @Override
        public Double choice(List<Double> in) {
            return in.stream()
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getAverage();
        }

        @Override
        public Double ifLeaf(ChoicePossibilities cp) {
            return cp.getDerivationPhrases().stream()
                    .map(this::getPhraseWeight)
                    .collect(Collectors.summarizingDouble(Double::doubleValue))
                    .getAverage();
        }

        /**
         * Weight of a PossibilityTree Leaf.
         * Calculated as the size as represented by number of Non-Terminals remaining.
         * @param p
         * @return
         */
        private double getPhraseWeight(Phrase p) {
            return p.stream()
                    .filter(it -> it.getBuilder().getType() == Variable.Type.NON_TERMINAL)
                    .toList()
                    .size();
        }
    }
}
