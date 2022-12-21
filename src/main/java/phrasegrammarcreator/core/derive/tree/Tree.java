package phrasegrammarcreator.core.derive.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tree<T> {

    private Node<T> root;
    private Node<T> head;
    public Tree(T data) {
        root = new Node<>(data, null);
        head = root;
    }

    public Tree(Node<T> ofNode) {
        root = ofNode;
        head = root;
    }
    public Node<T> getRoot() {
        return root;
    }
    public List<Node<T>> getLayer(int i) {
        List<Node<T>> layerNodes = new ArrayList<>();
        TreeFunction<T> function = (node, depth) -> {
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
    public List<Node<T>> getBottomNodes() {
        List<Node<T>> bottomNodes = new ArrayList<>();
        TreeFunction<T> addBottomNodes = (node, depth) -> {
            if (node.isLeaf()) {
                bottomNodes.add(node);
            }
            return true;
        };
        breadthFirstSearch(addBottomNodes);
        return bottomNodes;
    }

    public Node<T> getHead() {
        return head;
    }
    public void setHead(Node<T> node) {
        if (!contains(node))
            return;
        head = node;
    }

    public List<Pointer<T>> getHeadPointer() {
        return head.getPointer();
    }
    public List<Node<T>> getPathOf(Node<T> node) {
        if (!contains(node))
            return null;
        Stack<Node<T>> path = new Stack<>();
        Node<T> current = node;
        while (current.getParent() != null) {
            path.push(current);
            current = current.getParent();
        }
        path.push(root);
        return new ArrayList<>(path);
    }

    public boolean contains(Node<T> node) {
        Node<T> upmostParent = node;
        while (upmostParent.getParent() != null) {
            upmostParent = node.getParent();
        }
        return root == upmostParent;
    }

    public int getCount() {
        // Box integer into array to retrieve a 'pointer'
        final int[] count = {0};
        TreeFunction<T> countFunction = (node, depth) -> {
            count[0]++;
            return true;
        };
        breadthFirstSearch(countFunction);
        return count[0];
    }

    public void breadthFirstSearch(TreeFunction<T> function) {
        List<Node<T>> current = List.of(root);
        int depth = 0;
        while (true) {
            List<Node<T>> nextLevel = new ArrayList<>();
            for (Node<T> node : current) {
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

    public void depthFirstSearch(TreeFunction<T> function) {
        depthFirstSearch(root, function, 0);
    }

    private boolean depthFirstSearch(Node<T> node, TreeFunction<T> function, int depth) {
        List<Node<T>> children = node.getChildren();
        // If root, execute function
        if (children.isEmpty()) {
            return function.execute(node, depth);
        }
        // Dig deeper
        for (Node<T> c: children) {
            if (!depthFirstSearch(c, function, depth + 1))
                // Abort traversing if function returns false
                return false;
        };
        // After processing each leaf, process this node
        return function.execute(node, depth);
    }




}
