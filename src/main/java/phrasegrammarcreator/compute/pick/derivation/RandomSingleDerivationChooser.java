package phrasegrammarcreator.compute.pick.derivation;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.main.Randomizer;

import java.util.List;
import java.util.Random;

public class RandomSingleDerivationChooser extends DerivationChooser {

    Random random;
    public RandomSingleDerivationChooser(List<Rule> rules) {
        super(rules);
        random = Randomizer.getInstance();
    }

    @Override
    public Derivation pick(DerivationSet derivationSet) {
        // Pick one random element
        return derivationSet.getRandom();
    }
}
