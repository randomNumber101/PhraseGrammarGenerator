package phrasegrammarcreator.core.phrases.variables;


public class Terminal extends Variable {

    public Terminal(String regex) {
        super(regex);
    }

    public Terminal( String regex, String name) {
        super(regex, name);
    }

    @Override
    public Type getType() {
        return Type.TERMINAL;
    }

    @Override
    public VariableInstance<Terminal> createInstance() {
        return new VariableInstance<Terminal>(this);
    }
}
