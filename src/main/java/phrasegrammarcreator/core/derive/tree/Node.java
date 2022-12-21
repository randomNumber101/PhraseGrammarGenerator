package phrasegrammarcreator.core.derive.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T, P extends Pointer> {
    protected T data;
    protected Node<T,P> parent;
    protected List<P> children;

    public Node(T data, Node<T,P> parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public Node<T,P> getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }
    public List<Node<T,P>> getChildren() {
        return getPointer().stream()
                .filter(Pointer::isInitialized)
                .map(p -> (Node<T,P>) p.getPointingTo())
                .toList();
    }

    public List<P> getPointer () {
        return children;
    }

    public void buildChild(P pointer) {
        getPointer().add(pointer);
        pointer.build(this);
    }

    public void addPointer(P pointer) {
        getPointer().add(pointer);
    }
}
