package phrasegrammarcreator.util;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
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
        Iterator<int[]> combinationIter = combinations(posCounts);

        return getCombination(combinationIter, loaded);
    }

    public static <T> Iterator<T> getEmpty() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }

    public static <T> List<T> loadAll(Iterator<T> iterator) {
        List<T> out = new ArrayList<>();
        while (iterator.hasNext())
            out.add(iterator.next());
        return out;
    }

    public static Iterator<int[]> combinations(int[] posCounts) {

        for (int i : posCounts)
            if (i <= 0)
                return getEmpty();
        return new Iterator<int[]>() {

            long current = 0;
            long cap = calculateCap();
            long[] frequency = calculateFrequencies();

            private long calculateCap() {
                long product = 1;
                for (int p : posCounts)
                    product *= p;
                return product;
            }

            private long[] calculateFrequencies() {
                long[] products = new long[Math.max(posCounts.length, 1)];
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
            public int[] next() {
                if (current >= cap)
                    throw new IndexOutOfBoundsException("No more combinations found.");

                int[] combination = new int[posCounts.length];
                for (int i = 0; i < posCounts.length; i++) {
                    if (frequency[i] == 0 || posCounts[i] == 0)
                        System.err.println("(In-)Sanity check: " + frequency[i]);

                    combination[i] = Math.toIntExact((current / frequency[i]) % posCounts[i]);
                }
                current++;
                return combination;
            }
        };
    }
    public static <T> Iterator<List<T>> getCombination(Iterator<int[]> combination, List<T>[] data) {
        return apply(combination, indices ->
                IntStream.range(0, data.length)
                .mapToObj(i -> data[i].get(indices[i]))
                .toList());
    }

    public static <T> Iterator<T> randomOrder(Random random, Collection<T> elements) {
        ArrayList<T> residual = new ArrayList<>(elements);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !residual.isEmpty();
            }

            @Override
            public T next() {
                T pick = residual.get(random.nextInt(residual.size()));
                residual.remove(pick);
                return pick;
            }
        };
    }

    public static <E> List<E> pickNRandomElements(List<E> immutable, int n, Random r) {
        ArrayList<E> list = new ArrayList<>(immutable);
        int length = list.size();
        if (length <= n || length == 0)
            return list;
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
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

    public static <T> Iterator<T> restrictTo(int maxNum, Iterator<T> it) {
        return new Iterator<T>() {

            private int num = 0;
            @Override
            public boolean hasNext() {
                return num < maxNum && it.hasNext();
            }

            @Override
            public T next() {
                if (num >= maxNum)
                    return null;
                num++;
                return it.next();
            }
        };
    }
}
