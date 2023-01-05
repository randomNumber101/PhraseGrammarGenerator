package phrasegrammarcreator.compute.pick.words;

import phrasegrammarcreator.core.phrases.variables.Variable;

public class VariableNameWordGenerator extends WordGenerator<Variable> {
    @Override
    public String nextString(Variable variable) {
        return variable.getName();
    }
}
