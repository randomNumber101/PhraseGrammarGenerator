package phrasegrammarcreator.core.phrases;


import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.Collection;
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

    public SubPhrase getSubphrase(Occurrence interval) {
        if (interval.to > this.size())
            return null;
        return new SubPhrase(this, interval);
    }

    public Phrase deriveBy(Derivation derivation) {
       Rule rule = derivation.getRule();
       Occurrence occurrence = derivation.getOccurence();
       if (occurrence.from < 0 || occurrence.to > this.size())
           throw new IndexOutOfBoundsException
                   (String.format("Cannot derive phrase. Interval (%d,%d) out of bounds (0,%d)",
                           occurrence.from, occurrence.to, this.size()));

       // Split variable list in parts before and after occurrences and merge them using the derived sub phrase.
       List<Variable> currentVariables = getInstanceBuilders(this);
       List<Variable> derivedSubPart = getInstanceBuilders(rule.getTarget().toPhrase());

       List<Variable> beforeOccurrence = currentVariables.subList(0, occurrence.from);
       List<Variable> afterOccurrence = currentVariables.subList(occurrence.to, size());

       ArrayList<Variable> rejoined = new ArrayList<>();
       rejoined.addAll(beforeOccurrence);
       rejoined.addAll(derivedSubPart);
       rejoined.addAll(afterOccurrence);


       return new Phrase(rejoined);
    }


    public List<Variable> getInstanceBuilders(Collection<VariableInstance> variableInstances) {
        return variableInstances.stream().map(vi -> (Variable) vi.getBuilder()).toList();
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
