package phrasegrammarcreator.main.pipeline;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.main.GenerationInstance;

import java.util.Collection;
import java.util.function.Function;

public abstract class AbstractPipe<I,O> implements Function<I, O> {

    @NotNull
    public <O2> AbstractPipe<I,O2> andThen(AbstractPipe<O,O2> after) {
        return new AbstractPipe<I, O2>() {
            @Override
            public O2 apply(I i) {
                return after.apply(AbstractPipe.this.apply(i));
            }
        };
    }

}
