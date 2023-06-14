package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;

public class ProductPossibilities extends Possibilities {

    private CfRuleContainer rc;

    protected ArrayList<ChoicePossibilities> factors;


    public ProductPossibilities(CfRuleContainer rc, Phrase p) {
        this.rc = rc;
        factors = new ArrayList<>();
        for(VariableInstance<?> vi : p) {
            factors.add(new ChoicePossibilities(rc, vi));
        }
    }

    public <S,T> S accept(PossibilityTreeAggregator<S,T> aggregator) {
        return aggregator.product(factors.stream().map(cp -> cp.accept(aggregator)).toList());
    }

    public <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator) {
        return accept(aggregator);
    }

    @Override
    public void acceptUniform(PossibilityTreeTraverser traverser) {
        traverser.product(this);
    }
}
