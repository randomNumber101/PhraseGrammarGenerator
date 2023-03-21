package phrasegrammarcreator.core.derive.possibilities;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductPossibilities extends Possibilities {

    private CfRuleContainer rc;

    protected ArrayList<ChoicePossibilities> possibilities;


    public ProductPossibilities(CfRuleContainer rc, Phrase p) {
        this.rc = rc;
        possibilities = new ArrayList<>();
        for(VariableInstance vi : p) {
            List<Phrase> derivations =
                    vi.getBuilder().getType() == Variable.Type.NON_TERMINAL ?
                            rc.getPhrasesFor(vi.getBuilder())
                            : List.of(vi.getBuilder().toPhrase()); // Non-Terminals will be derived as themselves
            possibilities.add(new ChoicePossibilities(rc, vi.getBuilder(), derivations));
        }
    }

    @Override
    public long getCount() {
        long product = 1;
        for (Possibilities p: possibilities) {
            product *= p.getCount();
        }
        return product;
    }

    @Override
    public void calculateNext() {
        possibilities.forEach(Possibilities::calculateNext);
    }

    @Override
    public List<Phrase> collectAll() {
        List<List<Phrase>> singlePossibilities = possibilities.stream().map(Possibilities::collectAll).toList();
        List<Phrase> out = cartesianProduct(singlePossibilities).stream().map(Phrase::join).toList();
        return out;
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

    @NotNull
    @Override
    public Iterator<Phrase> iterator() {

        Iterator<List<Phrase>> combinations =
                IteratorTools.combine(possibilities.stream().map(ChoicePossibilities::iterator).toList());
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
}
