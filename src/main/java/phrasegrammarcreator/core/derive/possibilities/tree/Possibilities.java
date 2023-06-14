package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.Iterator;
import java.util.Random;

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
        this.calculateNextConstrained(random, possibilityCap, depthCap);
    }

    public long calculateCurrentCount() {
        return acceptUniform(new PossibilityTreeAggregator.GetCurrentCountAggregator());
    }

    public long getNextCount(){
        return acceptUniform(new PossibilityTreeAggregator.GetNextCountAggregator());
    };

    public void calculateNext() {
        acceptUniform(LeafConsumer.calculateNextConsumer());
    };

    public void calculateNextConstrained(Random rand, int countCap, int depthCap) {
        acceptUniform(new PossibilityTreeTraverser.ConstrainedTreeExtender(rand, countCap, depthCap));
    }

    public Iterator<Phrase> iterator() {
        return acceptUniform(new PossibilityTreeAggregator.PhraseIteratorAggregator());
    }

    public abstract <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator);

    public abstract void acceptUniform(PossibilityTreeTraverser traverser);
}
