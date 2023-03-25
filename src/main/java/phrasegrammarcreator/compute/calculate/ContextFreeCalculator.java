package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.compute.SingleOccurrence;
import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
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
import java.util.stream.Collectors;

public class ContextFreeCalculator extends DerivationsCalculator{

    CfRuleContainer cfRuleContainer;
    public ContextFreeCalculator( CfRuleContainer ruleContainer) {
        super((List<Rule>)(List<?>) ruleContainer.getRules());
        cfRuleContainer = ruleContainer;
    }


    @Override
    public DerivationSet calculate(Phrase p, DerivationSet lastDerivations, Derivation lastPicked) {
        if (lastDerivations == null)
            return calculateNaive(p);

        // Remove ContextSensitive derivations
        DerivationSet derivations = lastDerivations.copy();
        derivations.removeAll(derivations.stream()
                .filter(d -> d.getRule().getSource().toPhrase().size() > 1)
                .collect(Collectors.toSet()));

        // Remove derivations that cross interval that has been derived
        Occurrence sourceInterval = lastPicked.getOccurence();
        derivations.deleteAllInSection(sourceInterval);

        // Shift derivations after interval
        int lengthChange = lastPicked.getRule().getLengthChange();
        Occurrence derivedInterval = sourceInterval.extendedBy(lengthChange);
        List<Derivation> sortedList = new ArrayList<>(List.of(derivations.getArray()));
        Collections.sort(sortedList);
        for (Derivation d : sortedList) {
            if (d.getOccurence().from >= sourceInterval.from) {
                d.shiftBy(lengthChange);
            }
        }

        // Recalculate derivations only in new interval
        SubPhrase sp = p.getSubphrase(derivedInterval);
        derivations.addAll(calculateNaive(sp).shiftedBy(derivedInterval.from));

        return derivations;
    }


    private DerivationSet calculateNaive(Phrase p) {
        DerivationSet out = new DerivationSet();
        for (int i = 0; i < p.size(); i++) {
            VariableInstance vi = p.get(i);
            if (vi.getType() == Variable.Type.NON_TERMINAL) {
                out.add(cfRuleContainer.getRulesFor(vi.getBuilder()), new SingleOccurrence(i));
            }
        }
        return out;
    }
}
