package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


public class BracketTreeGenerator extends OutputGenerator{

    public static final String OPEN = "(";
    public static final String CLOSE = ")";

    public static Variable DUMMY = new NonTerminal("DUMMY");

    public HashMap<VariableInstance<?>, Integer> depths;

    BracketCounter[] brackets;

    public BracketTreeGenerator(Random random, WordGenerationPolicy policy) {
        super(random, policy);
    }


    /*
     * Search in leafs for next highest nodes that are identical and create brackets based on all successive nodes
     * for which this condition is satisfied.
     * Gradually increase search height until tree depth is reached.
     *
     * Example:
     *
     *      A
     *     / \         1st iteration:   N1 and N2 have B in common  -> create B brackets around N1 and N2
     *    B   *        2nd iteration:  N1, N2, N3 have A in common  -> create A brackets around N1 and N3
     *   / \   \       done:            return (A (B N1 N2) N3)
     *  N1 N2  N3
     *
     * Note:
     *  For this to work we insert dummy nodes (marked as "*" here) and remove them afterwards.
     *
     * */
    @Override
    public void initialize(EndPhrase ep) {
        Phrase p = ep.getNode().getData();
        depths = new HashMap<>();
        int treeDepth = treeDepth(ep);

        // Initialize Bracket Counters and Offset by Node Depth Off-Set
        brackets = new BracketCounter[ep.size()];
        for (int i = 0; i < ep.size(); i++) {
            VariableInstance<?> node = p.get(i);
            int delta = treeDepth - nodeDepth(node);
            brackets[i] = new BracketCounter(0, 0);

            // Pad Nodes by Depth Delta
            createDummyNodes(node, delta);
        }

        int searchHeight = 1;
        VariableInstance<?> parent = null;

        while (searchHeight <= treeDepth) {
            for (int i = 0; i <= ep.size(); i++) {
                VariableInstance<? extends Variable> newParent = i < ep.size() ?
                            getParent(ep.getNode().getData().get(i), searchHeight)
                            : null;

                if (newParent != parent) {
                    if (newParent != null) {
                        // Skip iteration if newParent is Dummy-Node
                        if (newParent.getBuilder().equals(DUMMY))
                            continue;
                        // Else, if parent changed open new bracket
                        brackets[i].open(newParent.getBuilder());
                    }
                    // If old parent was set, close bracket
                    if (parent != null)
                        brackets[i - 1].close();
                    parent = newParent;
                }
            }
            parent = null;
            searchHeight++;
        }

        // Remove dummy nodes
        for (VariableInstance<? extends Variable> node : p) {
            // real node depth is saved in hashmap and will not consider dummy nodes
            int delta = treeDepth - nodeDepth(node);

            VariableInstance<?> realParent = getParent(node, delta + 1);
            node.setDerivedFrom(realParent);
        }
    }


    private void createDummyNodes(VariableInstance<?> node, int count) {
        VariableInstance<?> realParent = node.getDerivedFrom();
        VariableInstance<?> current = node;
        for (int i = 0; i < count; i++) {
            VariableInstance<?> dummy = DUMMY.createInstance();
            current.setDerivedFrom(dummy);
            current = dummy;
        }
        current.setDerivedFrom(realParent);
    }

    private String generateBrackets(List<String> parts, BracketCounter[] brackets) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            String bracketed =
                    brackets[i].printOpen()
                    + parts.get(i)
                    + brackets[i].printClosed();
            builder.append(bracketed);
        }
        return builder.toString();
    }


    private VariableInstance<?> getParent(VariableInstance<?> vi, int height) {
        if (vi == null)
            return null;

        VariableInstance<?> current = vi;
        for (int i = 0; i < height; i++) {
            current = current.getDerivedFrom();
            if (current == null)
                return null;
        }
        return current;
    }

    private int treeDepth(EndPhrase p) {
        int maxDepth = 0;
        for (VariableInstance<? extends Variable> vi : p.getNode().getData()) {
            maxDepth = Math.max(maxDepth, nodeDepth(vi));
        }
        return maxDepth;
    }

    private int nodeDepth(VariableInstance<?> instance) {
        if (depths.containsKey(instance))
            return depths.get(instance);

        int depth = 0;
        VariableInstance<?> current = instance.getDerivedFrom();
        while (current != null) {
            depth++;
            current = current.getDerivedFrom();
        }
        depths.put(instance, depth);
        return depth;
    }

    @Override
    protected Function<List<String>, String> getInputGenerator() {
        return parts -> String.join(" ", parts);
    }

    @Override
    protected Function<List<String>, String> getLabelGenerator() {
        return parts -> generateBrackets(parts, brackets);
    }


    private static class BracketCounter {

        List<String> bracketTypes;
        public BracketCounter(int openOffset, int closedOffset) {
            openBrackets = openOffset;
            closedBrackets = closedOffset;
            bracketTypes = new ArrayList<>();
        }
        private int openBrackets  = 0;
        private int closedBrackets = 0;

        public void open() {
            openBrackets++;
            bracketTypes.add("");
        }

        public void open(Variable type) {
            openBrackets++;
            bracketTypes.add(type.getName());
        }

        public void close() {
            closedBrackets++;
        }

        public int getOpenBrackets() {
            return openBrackets;
        }

        public int getClosedBrackets() {
            return closedBrackets;
        }

        public String printOpen() {
            assert openBrackets == bracketTypes.size();
            StringBuilder builder = new StringBuilder();
            for (int i = bracketTypes.size() - 1; i >= 0; i--) {
                String type = bracketTypes.get(i);
                builder.append(OPEN).append(type).append(" ");
            }
            return builder.toString();
        }

        public String printClosed() {
            return CLOSE.repeat(closedBrackets);
        }
    }
}
