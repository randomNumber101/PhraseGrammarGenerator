package phrasegrammarcreator.core.rules;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CfRuleContainer {

    private final List<ContextFreeRule> rules;
    HashMap<Variable, List<ContextFreeRule>> variableApplicableRules;

    public CfRuleContainer() {
        this.rules = new ArrayList<>();
    }

    public CfRuleContainer(List<Rule> rules) {
        this.rules = forceContextFree(rules);
        computeVariableRules();
    }

    public void computeVariableRules() {
        variableApplicableRules = new HashMap<>();
        for (ContextFreeRule cfr : this.rules) {
            NonTerminal source = cfr.getLHS();
            if (!variableApplicableRules.containsKey(source))
                variableApplicableRules.put(cfr.getLHS(), new ArrayList<>(List.of(cfr)));
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

    public List<ContextFreeRule> getRulesWithRHS(Phrase phrase) {
        List<ContextFreeRule> output = new ArrayList<>();
        for (ContextFreeRule rule : rules) {
            if (rule.getRHS().equalsByBuilders(phrase))
                output.add(rule);
        }
        return output;
    }

    public List<Phrase> getPhrasesFor(Variable v) {
        return getRulesFor(v).stream()
                    .map(ContextFreeRule::getRHS)
                    .map(Phrase::cleanCopy)
                    .toList();
    }

    public List<ContextFreeRule> getRules() {
        return rules;
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
