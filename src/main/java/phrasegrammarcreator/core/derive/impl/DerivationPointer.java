package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.core.derive.tree.Node;
import phrasegrammarcreator.core.derive.tree.Pointer;
import phrasegrammarcreator.core.phrases.Phrase;

public class DerivationPointer extends Pointer<Phrase> {

    private Phrase from;
    private Derivation derivation;

    public DerivationPointer(Phrase from, Derivation derivation) {
        super();
        this.from = from;
        this.derivation = derivation;
    }
    public DerivationPointer(Node<Phrase> node) {
        super(node);
    }

    @Override
    protected Node<Phrase> build(Node<Phrase> parent) {
        if (pointingTo != null)
            return pointingTo;
        else {
            Phrase derived = from.deriveBy(derivation);
            return new Node<>(derived, parent);
        }
    }
}