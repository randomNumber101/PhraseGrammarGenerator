package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.compute.SingleOccurence;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.SubPhrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ContextFreeCalculator extends DerivationsCalculator{

    HashMap<Variable, List<ContextFreeRule>> variableApplicableRules;
    protected ContextFreeCalculator(List<ContextFreeRule> cfRules) {
        super((List<Rule>)(List<?>) cfRules);
        variableApplicableRules = new HashMap<>();
        computeVariableRules(cfRules);
    }

    private void computeVariableRules(List<ContextFreeRule> cfRules) {
        for (ContextFreeRule cfr : cfRules) {
            NonTerminal source = cfr.getSource();
            if (!variableApplicableRules.containsKey(source))
                variableApplicableRules.put(cfr.getSource(), new ArrayList<>(List.of(cfr)));
            else
                variableApplicableRules.get(source).add(cfr);
        }
    }

    @Override
    public DerivationSet calculate(Phrase p, DerivationSet lastDerivation, Derivation lastPicked) {
        if (lastDerivation == null)
            return calculateNaive(p);

        DerivationSet derivations = new DerivationSet(lastDerivation);

        // Remove derivations that cross interval that has been derived
        Occurence sourceInterval = lastPicked.getOccurence();
        derivations.deleteAllInSection(sourceInterval);


        // Shift derivations after interval
        int lengthChange = lastPicked.getRule().getLengthChange();
        Occurence derivedInterval = new Occurence(sourceInterval.from, sourceInterval.to + lengthChange);
        List<Derivation> sortedList = new ArrayList<>(List.of(derivations.getArray()));
        Collections.sort(sortedList);
        for (Derivation d : sortedList) {
            if (d.getOccurence().from >= derivedInterval.to) {
                d.shift(lengthChange);
            }
        }

        // Recalculate derivations only in new interval
        SubPhrase sp = p.getSubphrase(derivedInterval);
        derivations.addAll(calculateNaive(sp));

        return derivations;
    }

    private DerivationSet calculateNaive(Phrase p) {
        DerivationSet out = new DerivationSet();
        for (int i = 0; i < p.size(); i++) {
            VariableInstance vi = p.get(i);
            if (vi.getType() == Variable.Type.NON_TERMINAL) {
                out.add(variableApplicableRules.get(vi.getBuilder()), new SingleOccurence(i));
            }
        }
        return out;
    }
}