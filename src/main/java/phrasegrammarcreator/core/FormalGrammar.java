package phrasegrammarcreator.core;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.calculate.ContextFreeCalculator;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.calculate.MixedCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.compute.pick.derivation.SmartChooser;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.derive.impl.SingleDerivationPointer;
import phrasegrammarcreator.core.derive.possibilities.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public class FormalGrammar{

    private String name;
    private Vocabulary vocabulary;
    private List<Rule> rules;

    private CfRuleContainer ruleContainer;
    private WordDictionary dictionary;
    private Phrase startPhrase;

    private DerivationsCalculator calculator;
    private DerivationChooser chooser;

    public FormalGrammar(String name, Vocabulary vocabulary, List<Rule> rules, WordDictionary dictionary, Phrase startPhrase) {
        this.name = name;
        this.vocabulary = vocabulary;
        this.rules = rules;
        this.dictionary = dictionary;
        this.startPhrase = startPhrase;
        this.ruleContainer = new CfRuleContainer(rules);

        calculator = new ContextFreeCalculator(ruleContainer);
        chooser = new SmartChooser(this);
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

    public CfRuleContainer getRuleContainer() {
        return ruleContainer;
    }
}
