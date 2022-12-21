package phrasegrammarcreator.compute.pick;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;
import java.util.Random;

public class RandomSingleDerivationChooser extends DerivationChooser {

    Random random;
    public RandomSingleDerivationChooser(List<Rule> rules) {
        super(rules);
        random = new Random();
    }

    @Override
    public Derivation pick(DerivationSet derivationSet) {
        // Pick one random element
        return derivationSet.getRandom();
    }
}
