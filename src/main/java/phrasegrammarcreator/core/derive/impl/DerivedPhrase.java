package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.core.derive.tree.Node;
import phrasegrammarcreator.core.phrases.Phrase;

public class DerivedPhrase extends Node<Phrase> {

    public DerivedPhrase(Phrase data, Node<Phrase> parent) {
        super(data, parent);
    }
}
