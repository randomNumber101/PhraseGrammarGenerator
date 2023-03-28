package phrasegrammarcreator.util.futures;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface SimpleFuture<T> extends Future<T> {

    @Override
    public default boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public default boolean isCancelled() {
        return false;
    }

    @Override
    public default boolean isDone() {
        return true;
    }

    @Override
    public default T get(long timeout, @NotNull TimeUnit unit) {
        return get();
    }

    public T get();
}
