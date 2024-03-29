package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.core.derive.tree.Node;
import phrasegrammarcreator.core.phrases.Phrase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DerivationNode extends Node<Phrase, SingleDerivationPointer> {
    private boolean calculated = false;

    private boolean fullyDerived = false;
    private DerivationNode parent;
    protected List<SingleDerivationPointer> children;

    public DerivationNode(Phrase data, DerivationNode parent) {
        super(data, parent);
        this.parent = parent;
        this.children = new ArrayList<>();
        super.children = children;
    }

    public DerivationNode derive(DerivationsCalculator calculator, DerivationChooser chooser) {
        DerivationSet derivations;
        if (!isCalculated()) {
            derivations = calculate(calculator);
        }
        else {
            derivations =
                    getPointer().stream().map(SingleDerivationPointer::getDerivation).collect(DerivationSet.toSet());
        }
        
        if (derivations.isEmpty())
            return null;

        Derivation chosen = chooser.pick(derivations);
        SingleDerivationPointer chosenPointer =
                getPointer().stream()
                        .filter(dp -> dp.getDerivation().equals(chosen))
                        .findFirst().orElseThrow();

        chosenPointer.initialize(this);
        DerivationNode child = chosenPointer.getPointingTo();
        return child;
    }


    public DerivationSet calculate(DerivationsCalculator calculator) {
        DerivationSet out;
        if (this.getParent() == null) {
            out = calculator.calculate(this.getData(), null, null);
        }
        else {
            DerivationSet parentDerivations =
                    this
                    .getParent()
                    .getPointer()
                    .stream()
                    .map(SingleDerivationPointer::getDerivation)
                    .collect(DerivationSet.toSet());

            Derivation picked =
                    this
                    .getParent()
                    .getPointer().stream()
                    .filter(dp -> dp.isInitialized() && dp.getPointingTo().equals(this))
                    .findFirst().orElseThrow()
                    .getDerivation();

            out = calculator.calculate(this.getData(), parentDerivations, picked);
        }
        this.deleteAllPointer();
        this.addAll(out);
        calculated = true;
        return out;
    }

    public DerivationSet getPossibleDerivations(DerivationsCalculator calculator) {
        if (!isCalculated())
            calculate(calculator);

        return getPointer().stream()
                .map(SingleDerivationPointer::getDerivation)
                .collect(DerivationSet.toSet());
    }

    public boolean isCalculated() {
        return calculated;
    }

    public DerivationNode getParent() {
        return parent;
    }

    public void deleteAllPointer() {
        children = new ArrayList<>();
    }

    public void addAll(Collection<Derivation> set) {
            set.forEach(derivation -> addPointer(new SingleDerivationPointer(data, derivation)));
    }

    @Override
    public List<SingleDerivationPointer> getPointer() {
        return children;
    }

    public String toString() {
        return this.getData().toString(" ");
    }

}
