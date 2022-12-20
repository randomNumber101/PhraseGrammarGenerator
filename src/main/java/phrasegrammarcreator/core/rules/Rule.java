package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrasable;

public abstract class Rule<T extends Phrasable, S extends Phrasable> {
    private final T derivationSource;
    private final S derivationTarget;
    private final int lengthChange;

    public Rule(T from, S to) {
        derivationSource = from;
        derivationTarget = to;
        lengthChange = from.toPhrase().size() - to.toPhrase().size();
    }

    public T getSource() {
        return derivationSource;
    }

    public S getTarget() {
        return derivationTarget;
    }

    public int getLengthChange() {
        return lengthChange;
    }

    public String toString() {
        return derivationSource.toString() + " -> " + derivationTarget.toString();
    }
}
