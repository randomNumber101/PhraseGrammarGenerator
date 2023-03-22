package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrasable;
import phrasegrammarcreator.core.phrases.Phrase;

public abstract class Rule<T extends Phrasable, S extends Phrasable> {

    private final String name;
    private final T derivationSource;
    private final S derivationTarget;
    private final int lengthChange;

    public Rule(String name, T from, S to) {
        this.name = name;
        derivationSource = from;
        derivationTarget = to;
        lengthChange = to.toPhrase().size() - from.toPhrase().size();
    }

    public T getSource() {
        return derivationSource;
    }

    public Phrase getTarget() {
        return derivationTarget.toPhrase().cleanCopy();
    }

    public int getLengthChange() {
        return lengthChange;
    }

    public String toString() {
        return derivationSource.toString() + " -> " + derivationTarget.toString();
    }

    public String getName() {
        return name;
    }
}
