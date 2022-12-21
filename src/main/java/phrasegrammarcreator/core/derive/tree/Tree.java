package phrasegrammarcreator.core.derive.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tree<T, P extends Pointer> {
    protected Node<T,P> root;
    protected Node<T,P> head;
    public Tree(T data) {
        root = new Node<>(data, null);
        head = root;
    }

    public Tree(Node<T,P> ofNode) {
        root = ofNode;
        head = root;
    }
    public Node<T,P> getRoot() {
        return root;
    }
    public List<Node<T,P>> getLayer(int i) {
        List<Node<T,P>> layerNodes = new ArrayList<>();
        TreeFunction<T,P> function = (node, depth) -> {
            if (depth > i) {
                return false;
            }
            if (depth == i)
                layerNodes.add(node);
            return true;
        };
        breadthFirstSearch(function);
        return layerNodes;
    }
    public List<Node<T,P>> getBottomNodes() {
        List<Node<T,P>> bottomNodes = new ArrayList<>();
        TreeFunction<T,P> addBottomNodes = (node, depth) -> {
            if (node.isLeaf()) {
                bottomNodes.add(node);
            }
            return true;
        };
        breadthFirstSearch(addBottomNodes);
        return bottomNodes;
    }

    public List<Node<T,P>> getPathOf(Node<T,P> node) {
        if (!contains(node))
            return null;
        Stack<Node<T,P>> path = new Stack<>();
        Node<T,P> current = node;
        while (current.getParent() != null) {
            path.push(current);
            current = current.getParent();
        }
        path.push(getRoot());
        return new ArrayList<>(path);
    }

    public boolean contains(Node<T,P> node) {
        Node<T,P> upmostParent = node;
        while (upmostParent.getParent() != null) {
            upmostParent = upmostParent.getParent();
        }
        return getRoot().equals(upmostParent);
    }

    public int getCount() {
        // Box integer into array to retrieve a 'pointer'
        final int[] count = {0};
        TreeFunction<T,P> countFunction = (node, depth) -> {
            count[0]++;
            return true;
        };
        breadthFirstSearch(countFunction);
        return count[0];
    }

    public void breadthFirstSearch(TreeFunction<T,P> function) {
        List<Node<T,P>> current = List.of(getRoot());
        int depth = 0;
        while (true) {
            List<Node<T,P>> nextLevel = new ArrayList<>();
            for (Node<T,P> node : current) {
                nextLevel.addAll(node.getChildren());
                if (!function.execute(node, depth))
                    return;
            }
            if (nextLevel.isEmpty())
                return;
            depth++;
            current = nextLevel;
        }
    }

    public void depthFirstSearch(TreeFunction<T,P> function) {
        depthFirstSearch(getRoot(), function, 0);
    }

    private boolean depthFirstSearch(Node<T,P> node, TreeFunction<T,P> function, int depth) {
        List<Node<T,P>> children = node.getChildren();
        // If root, execute function
        if (children.isEmpty()) {
            return function.execute(node, depth);
        }
        // Dig deeper
        for (Node<T,P> c: children) {
            if (!depthFirstSearch(c, function, depth + 1))
                // Abort traversing if function returns false
                return false;
        };
        // After processing each leaf, process this node
        return function.execute(node, depth);
    }




}
