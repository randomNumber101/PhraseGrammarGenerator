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

    // TODO: Implement picking multiple derivations at once
    public abstract Derivation pick(DerivationSet pd);
}
