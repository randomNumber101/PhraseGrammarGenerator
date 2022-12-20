package phrasegrammarcreator.compute;

import phrasegrammarcreator.core.rules.Rule;
import org.jetbrains.annotations.NotNull;

public class Derivation implements Comparable<Derivation>{

    private Rule rule;
    private Occurence occurence;

    public Derivation(Rule rule, Occurence occurence) {
        this.rule = rule;
        this.occurence = occurence;
    }

    public Occurence getOccurence() {
        return occurence;
    }

    public Rule getRule() {
        return rule;
    }

    public void shift(int lengthChange) {
        occurence = occurence.shiftedBy(lengthChange);
    }

    @Override
    public int compareTo(@NotNull Derivation d) {
        return occurence.compareTo(d.getOccurence());
    }



}
