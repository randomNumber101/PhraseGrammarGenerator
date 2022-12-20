package phrasegrammarcreator.core.phrases.variables;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.List;

public class NonTerminal extends Variable {

    public NonTerminal(String regex) {
        super(regex);
    }

    public NonTerminal(String regex, String name) {
        super(regex, name);
    }

    @Override
    public Type getType() {
        return Type.NON_TERMINAL;
    }

    @Override
    public Phrase toPhrase() {
        return new Phrase(List.of(this));
    }

    @Override
    public VariableInstance<NonTerminal> createInstance(Phrase phrase) {
        return new VariableInstance<NonTerminal>(this, phrase);
    }
}
