package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.util.IteratorTools;

import java.util.*;

public class ChoicePossibilities extends Possibilities {


    CfRuleContainer rc;

    Phrase container;
    VariableInstance<?> from;
    List<Phrase> to;
    HashSet<Phrase> extended = new HashSet<>();

    protected ArrayList<ProductPossibilities> choices;

    public ChoicePossibilities(CfRuleContainer rc, VariableInstance<?> from) {
        List<Phrase> derivations =
                from.getBuilder().getType() == Variable.Type.NON_TERMINAL ?
                        rc.getPhrasesFor(from.getBuilder())
                        : new ArrayList<>(); // Non-Terminals will be derived as themselves

        this.from = from;
        this.to = derivations;
        this.rc = rc;
        container = new Phrase();
        container.add(from);
    }

    public <S,T> T accept(PossibilityTreeAggregator<S,T> aggregator) {
        if (choices == null || choices.isEmpty())
            return aggregator.ifLeaf(this);
        return aggregator.choice(choices.stream().map(pp -> pp.accept(aggregator)).toList());
    }

    public <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator) {
        return accept(aggregator);
    }

    @Override
    public void acceptUniform(PossibilityTreeTraverser traverser) {
        traverser.choice(this);
    }

    public Phrase getContainter() {
        return container;
    }

    public List<Phrase> getDerivationPhrases() {
        return to;
    }

    public List<Phrase> getExtendable() {
        ArrayList<Phrase> all = new ArrayList<>(to);
        all.removeAll(extended);
        return all;
    }

    public boolean extend(Phrase p) {
        if (extended.contains(p) |! to.contains(p))
            return false;
        if (choices == null)
            choices = new ArrayList<>();
        choices.add(new ProductPossibilities(rc, p));
        p.setDerivedFromAll(from);
        extended.add(p);
        return true;
    }


}
