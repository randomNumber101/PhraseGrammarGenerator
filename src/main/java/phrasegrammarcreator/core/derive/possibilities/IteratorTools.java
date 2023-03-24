package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IteratorTools {


    public static <I,O> Iterator<O> apply(Iterator<I> it, Function<I,O> function) {
        return new Iterator<O>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public O next() {
                return function.apply(it.next());
            }
        };
    }

    public static  <T> Iterator<T> concat(List<Iterator<T>> iterators) {
        Stream<T> concatenated = iterators.stream().flatMap(iterator ->
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false));
        return concatenated.iterator();
    }

    public static  <T> Iterator<T> concat(Iterator<Iterator<T>> iterators) {
        Stream<T> stream = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(iterators, Spliterator.ORDERED), true)
                .flatMap(it -> StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false)
                );
        return stream.iterator();
    }


    public static <T> Iterator<List<T>> combine(List<Iterator<T>> iterators) {
        int count = iterators.size();
        List<T>[] loaded = new ArrayList[count];
        for (int i = 0; i < count; i++) {
            loaded[i] = loadAll(iterators.get(i));
        }

        int[] posCounts = Arrays.stream(loaded).mapToInt(List::size).toArray();
        Iterator<Integer[]> combinationIter = combinations(posCounts);

        return new Iterator<List<T>>() {
            @Override
            public boolean hasNext() {
                return combinationIter.hasNext();
            }

            @Override
            public List<T> next() {
                List<T> out = new ArrayList<>();
                Integer[] nextCombination = combinationIter.next();

                for (int i = 0; i < count; i++) {
                    out.add(loaded[i].get(nextCombination[i]));
                }

                return out;
            }
        };
    }


    public static <T> List<T> loadAll(Iterator<T> iterator) {
        List<T> out = new ArrayList<>();
        while (iterator.hasNext())
            out.add(iterator.next());
        return out;
    }


    public static Iterator<Integer[]> combinations(int[] posCounts) {
        return new Iterator<Integer[]>() {

            long current = 0;
            long cap = calculateCap();
            int[] frequency = calculateFrequencies();

            private long calculateCap() {
                long product = 1;
                for (int p : posCounts)
                    product *= p;
                return product;
            }

            private int[] calculateFrequencies() {
                int[] products = new int[posCounts.length];
                products[0] = 1;
                for (int i = 1; i < posCounts.length; i++) {
                    products[i] = products[i - 1] * posCounts[i - 1];
                }
                return products;
            }

            @Override
            public boolean hasNext() {
                return current < cap;
            }

            @Override
            public Integer[] next() {
                if (current >= cap)
                    throw new IndexOutOfBoundsException("No more combinations found.");

                Integer[] combination = new Integer[posCounts.length];
                for (int i = 0; i < posCounts.length; i++) {
                    combination[i] = Math.toIntExact((current / frequency[i]) % posCounts[i]);
                }
                current++;
                return combination;
            }
        };
    }

    public static List<List<Phrase>> cartesianProduct(List<List<Phrase>> lists) {
        if (lists == null || lists.isEmpty()) {
            return Collections.emptyList();
        } else if (lists.size() == 1) {
            List<List<Phrase>> result = new ArrayList<>();
            for (Phrase element : lists.get(0)) {
                List<Phrase> tuple = new ArrayList<>();
                tuple.add(element);
                result.add(tuple);
            }
            return result;
        } else {
            List<List<Phrase>> result = new ArrayList<>();
            List<Phrase> firstList = lists.get(0);
            List<List<Phrase>> remainingLists = lists.subList(1, lists.size());
            List<List<Phrase>> remainingTuples = cartesianProduct(remainingLists);
            for (Phrase element : firstList) {
                for (List<Phrase> tuple : remainingTuples) {
                    List<Phrase> newTuple = new ArrayList<>();
                    newTuple.add(element);
                    newTuple.addAll(tuple);
                    result.add(newTuple);
                }
            }
            return result;
        }
    }
}
