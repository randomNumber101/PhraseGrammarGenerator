package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.util.IteratorTools;

import java.lang.reflect.Array;
import java.util.*;

public abstract class Possibilities implements Iterable<Phrase> {

    public void buildTreeTillCap(Random random, int possibilityCap, int depthCap) {
        // Extend tree to the layer so that possibilities < possibilityCap
        for (int i = 0; i < depthCap; i++) {
            long count = this.getNextCount();
            if (count > possibilityCap)
                break;
            this.calculateNext();
        }

        // Partially extend tree so possibilities do not explode
        this.calculateNextConstrained(random, possibilityCap);
    }

    public long getCurrentCount() {
        return acceptUniform(PossibilityTreeAggregator.currentCount);
    }

    public long getNextCount(){
        return acceptUniform(PossibilityTreeAggregator.nextCount);
    };

    public void calculateNext() {
        acceptUniform(LeafConsumer.calculateNext());
    };

    public void calculateNextCapped(Random random, int maxNext) {acceptUniform(new PossibilityTreeTraverser.CappedNodeExtender(random, maxNext));}

    public List<ChoicePossibilities> getLeafs() {
        return acceptUniform(LeafConsumer.leafNodes);
    }

    public void calculateNextConstrained(Random rand, int countCap) {
        //acceptUniform(new PossibilityTreeTraverser.LogarithmicConstrainedTreeExtender(rand, countCap, depthCap));
        ArrayList<ChoicePossibilities> leafs = new ArrayList<>(getLeafs());

        // Extend each leaf by one random child
        List<ChoicePossibilities> singleOrNoChildLeafs = leafs.stream().filter(l -> l.to.size() < 2).toList();
        leafs.forEach(l -> l.calculateNextCapped(rand, 1));
        leafs.removeAll(singleOrNoChildLeafs);

        List<ChoicePossibilities> extendables = new ArrayList<>(leafs);
        while(getCurrentCount() < countCap &! extendables.isEmpty()) {
            extendables = extendables.stream().filter(leaf -> !leaf.getExtendable().isEmpty()).toList();
            Iterator<ChoicePossibilities> iterator = IteratorTools.randomOrder(rand, extendables);
            for (Iterator<ChoicePossibilities> it = iterator; it.hasNext(); ) {
                ChoicePossibilities leaf = it.next();
                // Per step the total number of possibilities may only quadruple
                leaf.calculateNextCapped(rand, 4);
                if (getCurrentCount() >= countCap)
                    return;
            }
        }
    }

    public Iterator<Phrase> iterator() {
        return acceptUniform(PossibilityTreeAggregator.phraseIterator);
    }

    public abstract <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator);

    public abstract void acceptUniform(PossibilityTreeTraverser traverser);
}
