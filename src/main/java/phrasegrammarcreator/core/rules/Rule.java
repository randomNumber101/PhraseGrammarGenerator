package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrasable;
import phrasegrammarcreator.core.phrases.Phrase;

public abstract class Rule<L extends Phrasable, R extends Phrasable> {

    private final String name;
    private final L leftHandSide;
    private final R rightHandSide;
    private final int lengthChange;

    public Rule(String name, L from, R to) {
        this.name = name;
        leftHandSide = from;
        rightHandSide = to;
        lengthChange = to.toPhrase().size() - from.toPhrase().size();
    }

    public L getLHS() {
        return leftHandSide;
    }

    public Phrase getRHS() {
        return rightHandSide.toPhrase().cleanCopy();
    }

    public int getLengthChange() {
        return lengthChange;
    }

    public String toString() {
        return leftHandSide.toString() + " -> " + rightHandSide.toPhrase().toString(" ");
    }

    public String getName() {
        return name;
    }
}
