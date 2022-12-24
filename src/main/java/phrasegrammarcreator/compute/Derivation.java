package phrasegrammarcreator.compute;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.core.rules.Rule;

public class Derivation implements Comparable<Derivation>{

    private Rule rule;
    private Occurrence occurrence;

    public Derivation(Rule rule, Occurrence occurrence) {
        this.rule = rule;
        this.occurrence = occurrence;
    }

    public Occurrence getOccurence() {
        return occurrence;
    }

    public Rule getRule() {
        return rule;
    }

    public void shiftBy(int lengthChange) {
        occurrence = occurrence.shiftedBy(lengthChange);
    }

    public void extendBy(int extension) {occurrence = occurrence.extendedBy(extension);}

    @Override
    public int compareTo(@NotNull Derivation d) {
        return occurrence.compareTo(d.getOccurence());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Derivation) {
            return rule.equals(((Derivation) other).getRule())
                    && occurrence.equals(((Derivation) other).occurrence);
        }
        else
            return super.equals(other);
    }
}
