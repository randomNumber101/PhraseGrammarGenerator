package phrasegrammarcreator.main.pipeline;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.core.derive.possibilities.IteratorTools;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.main.GenerationInstance;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class ForkPipe<I,O> extends AbstractPipe<I, Iterator<O>> {

    private final Function<I, Iterator<O>> generator;

    public ForkPipe(Function<I, Iterator<O>> generator) {
        this.generator = generator;
    }


    public <O2> ForkPipe<I, O2> andThen(ParallelPipe<O, O2> p) {
        if (p.single)
            return new ForkPipe<I, O2>(generator.andThen(os -> IteratorTools.apply(os, p.singleOut)));

        return new ForkPipe<I, O2>(generator.andThen(os ->
                IteratorTools.concat(IteratorTools.apply(os, p.multiOut)))
        );
    }

    @Override
    public Iterator<O> apply(I i) {
        return generator.apply(i);
    }
}
