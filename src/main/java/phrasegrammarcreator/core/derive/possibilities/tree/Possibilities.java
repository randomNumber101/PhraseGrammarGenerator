package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.Iterator;

public abstract class Possibilities implements Iterable<Phrase> {

    public long getCount(){
        return acceptUniform(new PossibilityTreeAggregator.GetCountAggregator());
    };

    public void calculateNext() {
        acceptUniform(LeafConsumer.calculateNextConsumer());
    };

    public Iterator<Phrase> iterator() {
        return acceptUniform(new PossibilityTreeAggregator.PhraseIteratorAggregator());
    }

    public abstract <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator);
}
