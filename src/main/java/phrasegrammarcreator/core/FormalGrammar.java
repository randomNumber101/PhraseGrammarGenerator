package phrasegrammarcreator.core;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.calculate.MixedCalculator;
import phrasegrammarcreator.compute.pick.DerivationChooser;
import phrasegrammarcreator.compute.pick.RandomSingleDerivationChooser;
import phrasegrammarcreator.core.derive.impl.DerivationPointer;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.variables.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public class FormalGrammar{

    private String name;
    private Vocabulary vocabulary;
    private List<Rule> rules;
    private WordDictionary dictionary;
    private Phrase startPhrase;

    private DerivationTree derivations;
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

        derivations = new DerivationTree(startPhrase);
        calculator = new MixedCalculator(rules);
        chooser = new RandomSingleDerivationChooser(rules);
    }

    public void next(){

    }

    public DerivationSet getNextPossibleDerivations() {
        if (!derivations.getHead().isCalculated()) {
            derivations.calculateHead(calculator);
        };
        return derivations.getHead().getPointer().stream()
                .map(DerivationPointer::getDerivation)
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
        return derivations;
    }
}
