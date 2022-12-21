package phrasegrammarcreator.core.derive.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private T data;
    private Node<T> parent;
    private List<Pointer<T>> children;

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }
    public List<Node<T>> getChildren() {
        return children.stream()
                .filter((p) -> p.isInitialized())
                .map(p -> p.getPointingTo())
                .toList();
    }

    public List<Pointer<T>> getPointer () {
        return children;
    }

    public void buildChild(Pointer<T> pointer) {
        children.add(pointer);
        pointer.build(this);
    }

    public void addPointer(Pointer<T> pointer) {
        children.add(pointer);
    }
}
