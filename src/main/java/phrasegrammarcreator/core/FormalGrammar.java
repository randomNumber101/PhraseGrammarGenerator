package phrasegrammarcreator.core;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.calculate.MixedCalculator;
import phrasegrammarcreator.compute.pick.DerivationChooser;
import phrasegrammarcreator.compute.pick.RandomSingleDerivationChooser;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.variables.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.List;

public class FormalGrammar{

    private String name;
    private Vocabulary vocabulary;
    private List<Rule> rules;

    private WordDictionary dictionary;
    private Phrase startPhrase;
    private List<Phrase> derivations;
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

        derivations = new ArrayList<>();
        calculator = new MixedCalculator(rules);
        chooser = new RandomSingleDerivationChooser(rules);
    }

    public void next(){

        if (derivations.isEmpty()) {

        }
    }

    public DerivationSet getNextPossibleDerivations() {
        if (derivations.isEmpty()) {
            derivations.add(startPhrase);
        }
        Phrase head = derivations.get(derivations.size() - 1);
        return calculator.calculate(head, lastDerivation, lastPicked);
    }


}
