package phrasegrammarcreator.util.pipeline;

import phrasegrammarcreator.util.IteratorTools;

import java.util.Iterator;
import java.util.function.Function;

public class ParallelPipe<I,O> extends AbstractPipe<Iterator<I>, Iterator<O>>{

    boolean single;
    Function<I,O> singleOut;
    ForkPipe<I,O> multiOut;


    public ParallelPipe(Function<I,O> function) {
        this.singleOut = function;
        single = true;
    }

    public ParallelPipe(ForkPipe<I,O> function) {
        this.multiOut = function;
        single = false;
    }

    @Override
    public Iterator<O> apply(Iterator<I> is) {
        if (single) {
            return IteratorTools.apply(is, singleOut);
        }
        else {
            return IteratorTools.concat(IteratorTools.apply(is, multiOut));
        }
    }


    public <O2> ParallelPipe<I,O2> andThen(ParallelPipe<O,O2> p) {
        if (single && p.single)
           return new ParallelPipe<>(singleOut.andThen(p.singleOut));
        else if (single) {
            return new ParallelPipe<>(new ForkPipe<>(singleOut.andThen(p.multiOut)));
        }
        else  {
            return new ParallelPipe<>(multiOut.andThen(p));
        }
    }
}
