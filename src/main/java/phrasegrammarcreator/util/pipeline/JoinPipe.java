package phrasegrammarcreator.util.pipeline;

import java.util.Iterator;
import java.util.function.Function;

public class JoinPipe<I,O> extends AbstractPipe<Iterator<I>, O>{

    private final Function<Iterator<I>, O> function;

    public JoinPipe(Function<Iterator<I>, O> function) {
        this.function = function;
    }
    @Override
    public O apply(Iterator<I> is) {
        return function.apply(is);
    }
}
