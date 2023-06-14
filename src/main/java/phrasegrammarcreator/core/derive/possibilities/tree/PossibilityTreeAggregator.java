package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.util.IteratorTools;
import phrasegrammarcreator.core.phrases.Phrase;

import java.util.Iterator;
import java.util.List;

 public abstract class PossibilityTreeAggregator<P, C> {
    public abstract P product(List<C> in);
    public abstract C choice(List<P> in);
    public abstract C ifLeaf(ChoicePossibilities cp);

    public static class Null{
        private Null(){};
        public static Null get() {return new Null();};
    };


    public static class ConstrainedTreeExpander extends PossibilityTreeAggregator<Null, Null> {

        @Override
        public Null product(List<Null> in) {
            return null;
        }

        @Override
        public Null choice(List<Null> in) {
            return null;
        }

        @Override
        public Null ifLeaf(ChoicePossibilities cp) {
            return null;
        }
    }

    public static class PhraseIteratorAggregator extends PossibilityTreeAggregator<Iterator<Phrase>, Iterator<Phrase>>{
        @Override
        public Iterator<Phrase> product(List<Iterator<Phrase>> in) {
            Iterator<List<Phrase>> combinations = IteratorTools.combine(in);
            return new Iterator<Phrase>() {
                @Override
                public boolean hasNext() {
                    return combinations.hasNext();
                }

                @Override
                public Phrase next() {
                    return Phrase.join(combinations.next());
                }
            };
        }

        @Override
        public Iterator<Phrase> choice(List<Iterator<Phrase>> in) {
            return IteratorTools.concat(in);
        }
        @Override
        public Iterator<Phrase> ifLeaf(ChoicePossibilities cp) {
            return List.of(cp.container).iterator();
        }
    }
     public static class GetCurrentCountAggregator extends PossibilityTreeAggregator<Long, Long> {
         @Override
         public Long product(List<Long> in) {
             long product = 1;
             for (Long aLong : in) {
                 product *= aLong;
             }
             return product;
         }

         @Override
         public Long choice(List<Long> in) {
             long sum = 0;
             for (Long aLong : in) {
                 sum += aLong;
             }
             return sum;
         }

         @Override
         public Long ifLeaf(ChoicePossibilities cp) {
             return 1L;
         }
     }
    public static class GetNextCountAggregator extends PossibilityTreeAggregator<Long, Long> {
        @Override
        public Long product(List<Long> in) {
            long product = 1;
            for (Long aLong : in) {
                product *= aLong;
            }
            return product;
        }

        @Override
        public Long choice(List<Long> in) {
            long sum = 0;
            for (Long aLong : in) {
                sum += aLong;
            }
            return sum;
        }

        @Override
        public Long ifLeaf(ChoicePossibilities cp) {
            return (long) Math.max(cp.to.size(), 1);
        }
    }

}
