package phrasegrammarcreator.core.phrases;


import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.List;

public class Phrase extends ArrayList<VariableInstance> implements Phrasable {



    public Phrase() {
    }

    public Phrase(List<Variable> variables) {
        for (Variable var : variables) {
            VariableInstance<?> instance = var.createInstance(this);
            this.add(instance);
        }
    }

    public SubPhrase getSubphrase(Occurence interval) {
        if (interval.to > this.size())
            return null;
        return new SubPhrase(this, interval);
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String separator) {
        return this
                .stream()
                .reduce("", (s, v) -> s + separator + v.toString(), (s, s2) -> s + s2);
    }

    @Override
    public Phrase toPhrase() {
        return this;
    }
}
