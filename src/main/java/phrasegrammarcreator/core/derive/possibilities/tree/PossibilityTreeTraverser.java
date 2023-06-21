package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.util.IteratorTools;

import java.util.*;

public abstract class PossibilityTreeTraverser {


    public void traverse(Possibilities p) {
        p.acceptUniform(this);
    }

    public abstract void choice(ChoicePossibilities choice);

    public abstract void product(ProductPossibilities factor);

    /**
     * @Deprecated
     */
    @Deprecated
    public static class LogarithmicConstrainedTreeExtender extends PossibilityTreeTraverser {

        private Random random;
        private final int maxNewLeaves;
        private final int maxDepth;

        public LogarithmicConstrainedTreeExtender(Random random, int maxNewLeaves, int maxDepth) {
            this.maxNewLeaves = maxNewLeaves;
            this.maxDepth = maxDepth;
            this.random = random;
        }

        private LogarithmicConstrainedTreeExtender splitOnChoices(int numChoices) {
            double division = (double) maxNewLeaves / (double) numChoices;
            int ceiled = (int) Math.ceil(division);
            return new LogarithmicConstrainedTreeExtender(random, ceiled, maxDepth - 1);
        }

        private LogarithmicConstrainedTreeExtender splitOnFactors(int numFactors) {
            double log = Math.log(maxNewLeaves) / Math.log(numFactors);
            int ceiled = Math.max((int) Math.ceil(log), 1);
            return new LogarithmicConstrainedTreeExtender(random, ceiled, maxDepth);
        }

        private void extendNodeCapped(ChoicePossibilities node) {
            node.choices = new ArrayList<>();
            List<Phrase> constrainedPicks = IteratorTools.pickNRandomElements(node.to, maxNewLeaves, random);
            for (Phrase p : constrainedPicks) {
                node.choices.add(new ProductPossibilities(node.rc, p));
                p.setDerivedFromAll(node.from);
            }
        }

        @Override
        public void choice(ChoicePossibilities choice) {
            if (choice.choices == null || choice.choices.isEmpty() || maxDepth <= 0) {
                extendNodeCapped(choice);
                return;
            }
            int numChoices = choice.choices.size();
            PossibilityTreeTraverser nextLayerTraverser = splitOnChoices(numChoices);
            choice.choices.forEach(p -> p.acceptUniform(nextLayerTraverser));
        }

        @Override
        public void product(ProductPossibilities product) {
            int numFactors = product.factors.size();
            PossibilityTreeTraverser nextLayerTraverser = splitOnFactors(numFactors);
            product.factors.forEach(p -> p.acceptUniform(nextLayerTraverser));
        }
    }


    /**
     * Extends a single choice node by max {@param maxNext} nodes.
     *
     */
    public static class CappedNodeExtender extends PossibilityTreeTraverser {

        private Random random;
        private int maxNext;

        public CappedNodeExtender(Random random, int maxNext) {
            this.random = random;
            this.maxNext = maxNext;
        }


        @Override
        public void choice(ChoicePossibilities cp) {
            HashSet<Phrase> notExtended = new HashSet<>(cp.to);
            notExtended.removeAll(cp.extended);

            if (notExtended.isEmpty())
                return;

            if (notExtended.size() <= maxNext) {
                notExtended.forEach(cp::extend);
                return;
            }

            Iterator<Phrase> randIt = IteratorTools.randomOrder(random, notExtended);
            for (int i = 0; i < maxNext; i++) {
                Phrase next = randIt.next();
                cp.extend(next);
            }
        }

        @Override
        public void product(ProductPossibilities cp) {
            cp.factors.forEach(this::choice);
        }
    }
}
