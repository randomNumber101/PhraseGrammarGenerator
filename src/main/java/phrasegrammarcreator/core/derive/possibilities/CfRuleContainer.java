package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CfRuleContainer {

    HashMap<Variable, List<ContextFreeRule>> variableApplicableRules;

    public CfRuleContainer(FormalGrammar grammar) {
        List<Rule> rules = grammar.getRules();
        computeVariableRules(forceContextFree(rules));
    }

    private void computeVariableRules(List<ContextFreeRule> cfRules) {
        variableApplicableRules = new HashMap<>();
        for (ContextFreeRule cfr : cfRules) {
            NonTerminal source = cfr.getSource();
            if (!variableApplicableRules.containsKey(source))
                variableApplicableRules.put(cfr.getSource(), new ArrayList<>(List.of(cfr)));
            else
                variableApplicableRules.get(source).add(cfr);
        }
    }

    public List<ContextFreeRule> getRulesFor(Variable v) {
        List<ContextFreeRule> out =  variableApplicableRules.get(v);
        if (out == null)
            return new ArrayList<>();
        return out;
    }

    public List<Phrase> getPhrasesFor(Variable v) {
        return getRulesFor(v).stream()
                    .map(ContextFreeRule::getTarget)
                    .map(Phrase::cleanCopy)
                    .toList();
    }

    public List<ContextFreeRule> forceContextFree(List<Rule> rules) {
        ArrayList<ContextFreeRule> out = new ArrayList<>();
        for (Rule r : rules) {
            if (r instanceof ContextFreeRule cfr)
                out.add(cfr);
            else
                throw new IllegalArgumentException("Rules need to be context free.");
        }
        return out;
    }

}
