package phrasegrammarcreator.core.phrases.variables;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Vocabulary {


    final HashMap<String, Variable> variables;

    public Vocabulary() {
        variables = new HashMap<>();
    }

    public Vocabulary(Vocabulary copy) {
        variables = new HashMap<>(copy.variables);
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

    public void setMaskWorthy(List<Terminal> terminalList) {
        terminalList.forEach(t -> t.maskWorthy = true);
    }

}
