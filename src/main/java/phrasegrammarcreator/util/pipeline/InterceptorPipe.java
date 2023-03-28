package phrasegrammarcreator.util.pipeline;

import java.util.function.Consumer;

public class InterceptorPipe<T> extends AbstractPipe<T,T> {

    private Consumer<T> consumer;

    public InterceptorPipe(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public T apply(T t) {
        consumer.accept(t);
        return t;
    }
}
