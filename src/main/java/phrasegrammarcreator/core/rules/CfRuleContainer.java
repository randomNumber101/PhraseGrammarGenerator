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
    HashMap<String, List<ContextFreeRule>> rhsHashs = new HashMap<>();

    public CfRuleContainer(List<Rule> rules) {
        this.rules = forceContextFree(rules);
        compute();
    }

    public void compute() {
        variableApplicableRules = new HashMap<>();
        for (ContextFreeRule cfr : this.rules) {
            NonTerminal lhs = cfr.getLHS();
            Phrase rhs = cfr.getRHS();
            if (!variableApplicableRules.containsKey(lhs))
                variableApplicableRules.put(cfr.getLHS(), new ArrayList<>(List.of(cfr)));
            else
                variableApplicableRules.get(lhs).add(cfr);

            String hash = hashPhrase(rhs);
            if (!rhsHashs.containsKey(hash))
                rhsHashs.put(hash, new ArrayList<>());
            rhsHashs.get(hash).add(cfr);
        }
    }

    public List<ContextFreeRule> getRulesFor(Variable v) {
        List<ContextFreeRule> out =  variableApplicableRules.get(v);
        if (out == null)
            return new ArrayList<>();
        return out;
    }

    private String hashPhrase(Phrase p) {
        return p.toString();
        /*
        StringBuilder builder = new StringBuilder();
        for (Variable v : p.transformToBuilders()) {
            builder.append(v.hashCode());
        }
        return builder.toString();
         */
    }

    public List<ContextFreeRule> getRulesWithRHS(Phrase phrase) {
        List<ContextFreeRule> result = rhsHashs.get(hashPhrase(phrase));
        if (result == null)
            result = new ArrayList<>();
        return result;
        /*
        List<ContextFreeRule> output = new ArrayList<>();
        for (ContextFreeRule rule : rules) {
            if (rule.getRHS().equalsByBuilders(phrase))
                output.add(rule);
        }
        return output;
         */
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
