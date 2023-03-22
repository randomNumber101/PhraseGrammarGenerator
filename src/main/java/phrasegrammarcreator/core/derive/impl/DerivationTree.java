package phrasegrammarcreator.core.derive.impl;

import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.core.derive.tree.Tree;
import phrasegrammarcreator.core.phrases.Phrase;

import java.util.List;
import java.util.Stack;

public class DerivationTree extends Tree<Phrase, SingleDerivationPointer> {

    private DerivationNode root;
    private DerivationNode head;
    public DerivationTree(Phrase data) {
        super(data);
        root = new DerivationNode(data, null);
        head = root;
    }

    public DerivationNode deriveHead(DerivationsCalculator calculator, DerivationChooser chooser) {
        head = derive(calculator, chooser, getHead());
        return head;
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
    public List<SingleDerivationPointer> getHeadPointer() {
        return head.getPointer();
    }

    public DerivationNode getRoot() {
        return root;
    }

    public DerivationPath getPathOf(DerivationNode node) {
        if (!contains(node))
            return null;
        Stack<DerivationNode> path = new Stack<>();
        DerivationNode current = node;
        while (current.getParent() != null) {
            path.push(current);
            current = current.getParent();
        }
        path.push(current);
        DerivationPath derivationPath = new DerivationPath();
        while (!path.empty()) {
            derivationPath.add(path.pop());
        }

        return derivationPath;
    }




}
