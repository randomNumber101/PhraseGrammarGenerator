package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.core.derive.tree.Node;
import phrasegrammarcreator.core.derive.tree.Pointer;
import phrasegrammarcreator.core.phrases.Phrase;

public class SingleDerivationPointer extends Pointer<Phrase, SingleDerivationPointer> {

    private Phrase from;
    private Derivation derivation;

    private DerivationNode pointingTo;

    public SingleDerivationPointer(Phrase from, Derivation derivation) {
        super();
        this.from = from;
        this.derivation = derivation;
        if (derivation.getOccurence().to > from.size())
            throw new IndexOutOfBoundsException(String.format(
                    "Derivation out of bounds: Occurence %s not in phrase %s",
                    derivation.getOccurence().toString(),
                    from.toString()));

    }
    public Phrase getFrom() {
        return from;
    }
    public Derivation getDerivation() {
        return derivation;
    }

    protected DerivationNode build(DerivationNode parent) {
        if (pointingTo != null)
            return pointingTo;
        else {
            Phrase derived = from.deriveBy(derivation);
            return new DerivationNode(derived, parent);
        }
    }
    @Override
    protected Node<Phrase, SingleDerivationPointer> build(Node<Phrase, SingleDerivationPointer> parent) {
        if (parent instanceof DerivationNode)
            return build((DerivationNode) parent);
        else {
            if (pointingTo != null)
                return pointingTo;
            else {
                Phrase derived = from.deriveBy(derivation);
                return new Node<>(derived, parent);
            }
        }

    }

    public DerivationNode getPointingTo() {
        return pointingTo;
    }

    public boolean isInitialized() {
        return pointingTo != null;
    }
    public void initialize(DerivationNode parent) {
        pointingTo = build(parent);
    }

}
