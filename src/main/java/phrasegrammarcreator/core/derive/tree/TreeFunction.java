package phrasegrammarcreator.core.derive.tree;

public interface TreeFunction<T> {

    /**
     * Applies function on node of depth and returns whether to keep traversing.
     * @param node the node
     * @param depth int
     * @return whether to keep traversing
     */
    public boolean execute(Node<T> node, int depth);
}
