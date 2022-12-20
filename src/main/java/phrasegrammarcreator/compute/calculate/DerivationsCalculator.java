package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public abstract class DerivationsCalculator {

    protected List<Rule> rules;

    protected DerivationsCalculator(List<Rule> rules) {
        this.rules = rules;
    }


    public abstract DerivationSet calculate(Phrase p, DerivationSet lastPossibleDerivations, Derivation lastPicked);
}
