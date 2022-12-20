package phrasegrammarcreator.core.phrases.variables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Vocabulary {

    private final HashMap<String, Variable> variables;

    public Vocabulary() {
        variables = new HashMap<>();
    }

    public void registerVariable(String regex, Variable variable)  {
        variables.put(regex, variable);
    }
    public Variable getVariable(String regex) {
        return variables.get(regex);
    }

    public Set<String> getRegexes() {
        return variables.keySet();
    }

    public Collection<Variable> getVariables() {
        return variables.values();
    }

    public String toString() {
        return variables.toString();
    }

}
