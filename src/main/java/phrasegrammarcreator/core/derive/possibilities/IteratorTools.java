package phrasegrammarcreator.core.derive.possibilities;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IteratorTools {


    public static  <T> Iterator<T> concat(List<Iterator<T>> iterators) {
        Stream<T> concatenated = iterators.stream().flatMap(iterator ->
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false));
        return concatenated.iterator();
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
}
