package phrasegrammarcreator.compute.pick;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public abstract class DerivationChooser {

    List<Rule> rules;
    public DerivationChooser(List<Rule> rules) {
        this.rules = rules;
    }

    public abstract List<Derivation> pick(DerivationSet pd);
}
