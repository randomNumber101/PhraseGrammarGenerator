package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

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
            brackets[i] = new BracketCounter(-delta, -delta);

            // Pad Nodes by Depth Delta
            createDummyNodes(node, delta);
        }

        /*
         * Search in leafs for next highest nodes that are identical and create brackets.
        * Gradually increase search height
        * */

        int layerHeight = 1;
        VariableInstance<?> parent = null;

        while (layerHeight <= treeDepth) {
            for (int i = 0; i <= ep.size(); i++) {
                VariableInstance<?> newParent = i < ep.size() ?
                            getParent(ep.getNode().getData().get(i), layerHeight)
                            : null;
                if (newParent != parent) {
                    if (newParent != null)
                        brackets[i].open();
                    if (parent != null)
                        brackets[i - 1].close();
                    parent = newParent;
                }
            }
            parent = null;
            layerHeight++;
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
                    OPEN.repeat(brackets[i].getOpenBrackets())
                    + parts.get(i)
                    + CLOSE.repeat(brackets[i].getClosedBrackets());
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
        public BracketCounter(int openOffset, int closedOffset) {
            openBrackets = openOffset;
            closedBrackets = closedOffset;
        }
        private int openBrackets  = 0;
        private int closedBrackets = 0;

        public void open() {
            openBrackets++;
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
    }
}
