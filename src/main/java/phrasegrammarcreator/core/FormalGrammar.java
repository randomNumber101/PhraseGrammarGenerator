package phrasegrammarcreator.core;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.calculate.MixedCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.compute.pick.derivation.RandomSingleDerivationChooser;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.impl.SingleDerivationPointer;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public class FormalGrammar{

    private String name;
    private Vocabulary vocabulary;
    private List<Rule> rules;
    private WordDictionary dictionary;
    private Phrase startPhrase;

    private DerivationTree derivationTree;
    private DerivationsCalculator calculator;
    private DerivationChooser chooser;
    private DerivationSet lastDerivation;
    private Derivation lastPicked;

    public FormalGrammar(String name, Vocabulary vocabulary, List<Rule> rules, WordDictionary dictionary, Phrase startPhrase) {
        this.name = name;
        this.vocabulary = vocabulary;
        this.rules = rules;
        this.dictionary = dictionary;
        this.startPhrase = startPhrase;

        derivationTree = new DerivationTree(startPhrase);
        calculator = new MixedCalculator(rules);
        chooser = new RandomSingleDerivationChooser(rules);
    }

    public void next(){
        derivationTree.deriveHead(calculator, chooser);
    }

    public DerivationSet getNextPossibleDerivations() {
        return getPossibleDerivations(getDerivationTree().getHead());
    }

    public DerivationSet getPossibleDerivations(DerivationNode node) {
        //if (!getDerivationTree().contains(node))
        //    // TODO: Force derivation of node
        //    throw new IllegalArgumentException("Node not part of grammar or not yet derived: " + node);
        if (!node.isCalculated()) {
            derivationTree.calculate(calculator, node);
        };
        return node.getPointer().stream()
                .map(SingleDerivationPointer::getDerivation)
                .collect(DerivationSet.toSet());
    }


    public List<Rule> getRules() {
        return rules;
    }

    public String getName() {
        return name;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public WordDictionary getDictionary() {
        return dictionary;
    }

    public Phrase getStartPhrase() {
        return startPhrase;
    }

    public DerivationTree getDerivationTree() {
        return derivationTree;
    }
}
