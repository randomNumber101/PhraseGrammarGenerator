package phrasegrammarcreator.core;

import phrasegrammarcreator.compute.calculate.ContextFreeCalculator;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.compute.pick.derivation.SmartChooser;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.List;

public class FormalGrammar{

    protected String name;
    protected Vocabulary vocabulary;
    protected List<Rule> rules;

    protected CfRuleContainer ruleContainer;
    protected WordDictionary dictionary;
    protected Phrase startPhrase;


    public FormalGrammar (){}
    public FormalGrammar(String name, Vocabulary vocabulary, List<Rule> rules, WordDictionary dictionary, Phrase startPhrase) {
        this.name = name;
        this.vocabulary = vocabulary;
        this.rules = rules;
        this.dictionary = dictionary;
        this.startPhrase = startPhrase;
        this.ruleContainer = new CfRuleContainer(rules);
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
