package phrasegrammarcreator.core.phrases.variables;

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
    public VariableInstance<NonTerminal> createInstance() {
        return new VariableInstance<NonTerminal>(this);
    }
}
