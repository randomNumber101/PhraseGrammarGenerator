package phrasegrammarcreator.util.futures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListFuture<T,S> implements SimpleFuture<S> {

    List<SimpleFuture<T>> futures = new ArrayList<>();

    private Function<List<T>,S> aggregator;

    public ListFuture(Function<List<T>,S> aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public S get() {
        return aggregator.apply(
                futures.stream()
                .map(SimpleFuture::get)
                .toList()
        );
    }

    public void add(SimpleFuture<T> val) {
        futures.add(val);
    }
}
