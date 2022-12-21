package phrasegrammarcreator.core.derive.tree;

public abstract class Pointer<T> {
    protected Node<T> pointingTo;

    public Pointer(){}
    public Pointer(Node<T> node) {
        pointingTo = node;
    }
    protected abstract Node<T> build(Node<T> parent);

    public boolean isInitialized() {
        return pointingTo != null;
    }
    public void initialize(Node<T> parent) {
        pointingTo = build(parent);
    }

    public Node<T> getPointingTo() {
        return pointingTo;
    }

}
