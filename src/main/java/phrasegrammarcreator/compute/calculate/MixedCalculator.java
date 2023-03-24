package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.ContextSensitiveRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.List;

public class MixedCalculator extends DerivationsCalculator{

    protected List<Rule> cfRules;
    protected List<ContextSensitiveRule> csRules;

    protected ContextFreeCalculator cfCalculator;
    protected ContextSensitiveCalculator csCalculator;

    public MixedCalculator(List<Rule> rules) {
        super(rules);
        cfRules = new ArrayList<>();
        csRules = new ArrayList<>();

        for (Rule r: rules) {
            if (r instanceof ContextFreeRule)
                cfRules.add(r);
            else if (r instanceof ContextSensitiveRule)
                csRules.add((ContextSensitiveRule) r);
        }
        cfCalculator = new ContextFreeCalculator(new CfRuleContainer(cfRules));
        csCalculator = new ContextSensitiveCalculator(csRules);
    }

    @Override
    public DerivationSet calculate(Phrase p, DerivationSet lastDerivations, Derivation lastPicked) {
        DerivationSet derivations = cfCalculator.calculate(p, lastDerivations, lastPicked);
        derivations.addAll(csCalculator.calculate(p, lastDerivations, lastPicked));

        return derivations;
    }

}
