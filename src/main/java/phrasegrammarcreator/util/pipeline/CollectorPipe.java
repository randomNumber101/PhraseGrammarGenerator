package phrasegrammarcreator.util.pipeline;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.util.IteratorTools;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CollectorPipe<T> extends AbstractPipe<Iterator<T>, Iterator<T>> {

    List<T> collected = new LinkedList<>();

    @Override
    public Iterator<T> apply(Iterator<T> iterator) {
        collected = IteratorTools.loadAll(iterator);
        return collected.iterator();
    }
}
