package phrasegrammarcreator.core.phrases.variables;


import phrasegrammarcreator.core.phrases.Phrase;

public class VariableInstance<T extends Variable> {

    private final T builder;
    private Phrase phrase;
    private VariableInstance<?> derivedFrom;

    public VariableInstance(T builder, Phrase phrase) {
        this.builder = builder;
        this.phrase = phrase;
    }

    public VariableInstance(T builder, Phrase phrase, VariableInstance<?> derivedFrom) {
        this.builder = builder;
        this.phrase = phrase;
        this.derivedFrom = derivedFrom;
    }

    public VariableInstance<?> getDerivedFrom() {
        return derivedFrom;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public T getBuilder() {
        return builder;
    }
    public Variable.Type getType() {
        return builder.getType();
    }

    public String toString() {
        return builder.toString();
    }
}
