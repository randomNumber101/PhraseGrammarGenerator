package phrasegrammarcreator.core.phrases.variables;


public class VariableInstance<T extends Variable> {

    private final T builder;
    private VariableInstance<?> derivedFrom;

    public VariableInstance(T builder) {
        this.builder = builder;
    }

    public VariableInstance<?> getDerivedFrom() {
        return derivedFrom;
    }

    public void setDerivedFrom(VariableInstance<?> parent) {
        derivedFrom = parent;
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof VariableInstance<?>) {
            return getBuilder().equals(((VariableInstance<?>) other).getBuilder());
        }
        else
            return super.equals(other);
    }

    public VariableInstance<?> getParent(int height) {
        VariableInstance<?> current = this;
        for (int i = 0; i < height; i++) {
            current = current.getDerivedFrom();
            if (current == null)
                return null;
        }
        return current;
    }

    public int getDepth() {
        int depth = 0;
        VariableInstance<?> current = this.getDerivedFrom();
        while (current != null) {
            depth++;
            current = current.getDerivedFrom();
        }
        return depth;
    }

}
