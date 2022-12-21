package phrasegrammarcreator.core.derive.tree;

public abstract class Pointer<T, P extends Pointer> {
    protected Node<T, P> pointingTo;

    public Pointer(){}
    public Pointer(Node<T, P> node) {
        pointingTo = node;
    }
    protected abstract Node<T,P> build(Node<T,P> parent);

    public boolean isInitialized() {
        return pointingTo != null;
    }
    public void initialize(Node<T,P> parent) {
        pointingTo = build(parent);
    }

    public Node<T,P> getPointingTo() {
        return pointingTo;
    }

}
