package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.List;

public class ChoicePossibilities extends Possibilities {


    CfRuleContainer rc;

    Phrase container;
    VariableInstance<?> from;
    List<Phrase> to;

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

    public Phrase getContainter() {
        return container;
    }

    public List<Phrase> getDerivationPhrases() {
        return to;
    }


}
