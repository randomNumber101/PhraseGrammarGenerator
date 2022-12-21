package phrasegrammarcreator.core.derive.tree;

public interface TreeFunction<T, P extends Pointer> {

    /**
     * Applies function on node of depth and returns whether to keep traversing.
     * @param node the node
     * @param depth int
     * @return whether to keep traversing
     */
    public boolean execute(Node<T, P> node, int depth);
}
