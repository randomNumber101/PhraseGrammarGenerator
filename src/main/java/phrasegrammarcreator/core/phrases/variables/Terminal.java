package phrasegrammarcreator.core.phrases.variables;


import phrasegrammarcreator.core.phrases.Phrase;

public class Terminal extends Variable {

    public Terminal(String regex) {
        super(regex);
    }

    public Terminal( String regex, String name) {
        super(regex, name);
    }

    @Override
    public Phrase toPhrase() {
        return null;
    }

    @Override
    public Type getType() {
        return Type.TERMINAL;
    }

    @Override
    public VariableInstance<Terminal> createInstance(Phrase phrase) {
        return new VariableInstance<Terminal>(this, phrase);
    }
}
