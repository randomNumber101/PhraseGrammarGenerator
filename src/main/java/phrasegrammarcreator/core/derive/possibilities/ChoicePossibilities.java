package phrasegrammarcreator.core.derive.possibilities;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChoicePossibilities extends Possibilities{


    CfRuleContainer rc;
    Variable from;
    List<Phrase> to;

    protected ArrayList<ProductPossibilities> choices;

    public ChoicePossibilities(CfRuleContainer rc, Variable from, List<Phrase> to) {
        this.from = from;
        this.to = to;
        this.rc = rc;
    }

    @Override
    public long getCount() {
        if (choices == null)
            return 1;

        int sum = 0;
        for (Possibilities p : choices)
            sum += p.getCount();
        return sum;
    }

    @Override
    public void calculateNext() {
        if (choices != null)
            choices.forEach(Possibilities::calculateNext);
        else {
            choices = new ArrayList<>();
            for (Phrase p : to) {
                choices.add(new ProductPossibilities(rc, p));
            }
        }
    }

    @Override
    public List<Phrase> collectAll() {
        if (choices == null)
            return List.of(from.toPhrase());

        List<Phrase> out = new ArrayList<>();
        for (Possibilities p : choices)
            out.addAll(p.collectAll());
        return out;
    }

    @NotNull
    @Override
    public Iterator<Phrase> iterator() {
        if (choices == null)
            return List.of(from.toPhrase()).iterator();
        return IteratorTools.concat(choices.stream().map(ProductPossibilities::iterator).toList());
    }
}
