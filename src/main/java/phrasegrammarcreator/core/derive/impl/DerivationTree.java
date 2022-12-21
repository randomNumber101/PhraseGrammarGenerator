package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.pick.DerivationChooser;
import phrasegrammarcreator.core.derive.tree.Tree;
import phrasegrammarcreator.core.phrases.Phrase;

import java.util.List;

public class DerivationTree extends Tree<Phrase, DerivationPointer> {

    private DerivationNode root;
    private DerivationNode head;
    public DerivationTree(Phrase data) {
        super(data);
        root = new DerivationNode(data, null);
        head = root;
    }

    public DerivationNode deriveHead(DerivationsCalculator calculator, DerivationChooser chooser) {
        return derive(calculator, chooser, getHead());
    }

    public void calculateHead(DerivationsCalculator calculator) {
        calculate(calculator, getHead());
    }

    public DerivationNode derive(DerivationsCalculator calculator, DerivationChooser chooser, DerivationNode node) {
        if (!contains(node))
            return null;
        return node.derive(calculator, chooser);
    }
    public DerivationSet calculate(DerivationsCalculator calculator, DerivationNode node) {
        if (!contains(node))
            return null;
        return node.calculate(calculator);
    }

    public DerivationNode getHead() {
        return head;
    }
    public void setHead(DerivationNode node) {
        if (!contains(node))
            return;
        head = node;
    }
    public List<DerivationPointer> getHeadPointer() {
        return head.getPointer();
    }

    public DerivationNode getRoot() {
        return root;
    }


}
