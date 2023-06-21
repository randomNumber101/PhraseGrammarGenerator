package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.util.IteratorTools;

import java.util.*;
import java.util.function.Consumer;

public class LeafConsumer extends PossibilityTreeAggregator<PossibilityTreeAggregator.Null, PossibilityTreeAggregator.Null> {

    private Consumer<ChoicePossibilities> consumer;

    public LeafConsumer(Consumer<ChoicePossibilities> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Null product(List<Null> in) {
        return Null.get();
    }

    @Override
    public Null choice(List<Null> in) {
        return Null.get();
    }
    @Override
    public Null ifLeaf(ChoicePossibilities cp) {
        consumer.accept(cp);
        return Null.get();
    }


    public static LeafConsumer calculateNext() {
        return new LeafConsumer(new Consumer<ChoicePossibilities>() {
            @Override
            public void accept(ChoicePossibilities cp) {
                cp.choices = new ArrayList<>();
                for (Phrase p : cp.to) {
                    cp.choices.add(new ProductPossibilities(cp.rc, p));
                    p.setDerivedFromAll(cp.from);
                }
            }
        });
    }

}
