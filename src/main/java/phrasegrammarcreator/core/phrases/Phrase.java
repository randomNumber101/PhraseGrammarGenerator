package phrasegrammarcreator.core.phrases;


import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Phrase extends ArrayList<VariableInstance<? extends Variable>> implements Phrasable {

    public Phrase() {
    }

    public Phrase(List<Variable> variables) {
        for (Variable var : variables) {
            VariableInstance<?> instance = var.createInstance();
            this.add(instance);
        }
    }

    public Phrase(List<VariableInstance<?>> is, boolean copy) {
        List<? extends VariableInstance<?>> instances = is;
        if (copy) {
                instances = is.stream()
                    .map(VariableInstance::getBuilder)
                    .map(Variable::createInstance)
                    .toList();
        }
        this.addAll(instances);
    }

    public Phrase cleanCopy() {
        return new Phrase(getInstanceBuilders(this));
    }

    public SubPhrase getSubphrase(Occurrence interval) {
        if (interval.to > this.size())
            return null;
        return new SubPhrase(this, interval);
    }

    public void setDerivedFromAll(VariableInstance<?> parent) {
        this.forEach(vi -> vi.setDerivedFrom(parent));
    }

    public void setDerivedFromInBound(VariableInstance<?> parent, Occurrence o) {
        if (o.from < 0 || o.to > size())
            throw new IndexOutOfBoundsException("Occurence is not within Phrase bounds.");
        for (int i = o.from; i < o.to; i++)
            this.get(i).setDerivedFrom(parent);
    }

    public Phrase deriveBy(Derivation derivation) {
       Rule rule = derivation.getRule();
       Occurrence occurrence = derivation.getOccurence();
       if (occurrence.from < 0 || occurrence.to > this.size())
           throw new IndexOutOfBoundsException
                   (String.format("Cannot derive phrase. Interval (%d,%d) out of bounds (0,%d)",
                           occurrence.from, occurrence.to, this.size()));

       // Split variable list in parts before and after occurrences and merge them using the derived sub phrase.
       List<VariableInstance<?>> beforeOccurrence = this.subList(0, occurrence.from);
       List<VariableInstance<?>> afterOccurrence = this.subList(occurrence.to, size());

       Phrase replacement = rule.getRHS();
       // TODO: This works only if ContextFreeGrammar
       replacement.setDerivedFromAll(this.get(occurrence.from));

       ArrayList<VariableInstance<?>> rejoined = new ArrayList<>();
       rejoined.addAll(beforeOccurrence);
       rejoined.addAll(replacement);
       rejoined.addAll(afterOccurrence);

       return new Phrase(rejoined, false);
    }


    public static List<Variable> getInstanceBuilders(Collection<VariableInstance<?>> variableInstances) {
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


    public static Phrase join(List<Phrase> phrases) {
        Phrase out = new Phrase();
        for (Phrase p : phrases)
            out.addAll(p);
        return out;
    }

    public List<Variable> transformToBuilders() {
        return (List<Variable>) this.stream().map(VariableInstance::getBuilder).toList();
    }

    public boolean equalsByBuilders(Phrase phrase) {
        List<Variable> myBuilders = transformToBuilders();
        List<Variable> otherBuilders = phrase.transformToBuilders();

        if (myBuilders.size() != otherBuilders.size())
            return false;
        else {
            for (int i = 0; i < myBuilders.size(); i++) {
                if (!myBuilders.get(i).equals(otherBuilders.get(i)))
                    return false;
            }
            return true;
        }
    }
}
