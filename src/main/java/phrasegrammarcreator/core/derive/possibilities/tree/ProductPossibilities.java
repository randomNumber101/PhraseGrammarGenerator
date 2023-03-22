package phrasegrammarcreator.core.derive.possibilities.tree;

import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
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

    @Override
    public long getCount() {
        long product = 1;
        for (Possibilities p: factors) {
            product *= p.getCount();
        }
        return product;
    }

    @Override
    public void calculateNext() {
        factors.forEach(Possibilities::calculateNext);
    }



    public <S,T> S accept(PossibilityTreeAggregator<S,T> aggregator) {
        return aggregator.product(factors.stream().map(cp -> cp.accept(aggregator)).toList());
    }

    public <T> T acceptUniform(PossibilityTreeAggregator<T,T> aggregator) {
        return accept(aggregator);
    }
}
