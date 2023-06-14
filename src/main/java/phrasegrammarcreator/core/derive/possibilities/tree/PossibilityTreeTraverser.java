package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.util.IteratorTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class PossibilityTreeTraverser {


    public void traverse(Possibilities p) {
        p.acceptUniform(this);
    }

    public abstract void choice(ChoicePossibilities choice);

    public abstract void product(ProductPossibilities factor);



    public static class ConstrainedTreeExtender extends PossibilityTreeTraverser {

        private Random random;
        private final int maxNewLeaves;
        private final int maxDepth;

        public ConstrainedTreeExtender(Random random, int maxNewLeaves, int maxDepth) {
            this.maxNewLeaves = maxNewLeaves;
            this.maxDepth = maxDepth;
            this.random = random;
        }

        private ConstrainedTreeExtender splitOnChoices(int numChoices) {
            double division = (double) maxNewLeaves / (double) numChoices;
            int ceiled = (int) Math.ceil(division);
            return new ConstrainedTreeExtender(random, ceiled, maxDepth - 1);
        }

        private ConstrainedTreeExtender splitOnFactors(int numFactors) {
            double log = Math.log(maxNewLeaves) / Math.log(numFactors);
            int ceiled = Math.max((int) Math.ceil(log), 1);
            return new ConstrainedTreeExtender(random, ceiled, maxDepth);
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
}
