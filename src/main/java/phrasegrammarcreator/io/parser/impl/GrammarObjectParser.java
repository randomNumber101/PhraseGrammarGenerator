package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.variables.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.JsonArrayParser;
import phrasegrammarcreator.io.parser.core.SingleValueParser;

import java.util.Collection;
import java.util.List;

public class GrammarObjectParser extends JSonObjectParser<FormalGrammar> {
    private SingleValueParser<NonTerminal> nonTerminalParser;
    private SingleValueParser<Terminal> terminalParser;
    private SingleValueParser<List<Rule>> ruleParser;
    private PhraseParser phraseParser;
    private DictionaryParser dictionaryParser;

    private Vocabulary vocabulary;

    public GrammarObjectParser() {
        vocabulary = new Vocabulary();
        nonTerminalParser = new NonTerminalParser(vocabulary);
        terminalParser = new TerminalParser(vocabulary);
        ruleParser = new RuleParser(vocabulary);
        phraseParser = new PhraseParser(vocabulary);
    }

    @Override
    public FormalGrammar parse(JSONObject object) throws Exception {

        // Parse name
        String name = object.getString("name");

        // Parse Non-Terminals, Terminals (adding them to the vocabulary)
        JsonArrayParser<String, NonTerminal> ntArrayParser = new JsonArrayParser<>(nonTerminalParser);
        List<NonTerminal> nonTerminals = ntArrayParser.parse(object.getJSONArray("non-terminals"));
        JsonArrayParser<String, Terminal> tArrayParser = new JsonArrayParser<>(terminalParser);
        List<Terminal> terminals = tArrayParser.parse(object.getJSONArray("terminals"));

        // Parse rules
        JsonArrayParser<String, List<Rule>> ruleArrayParser = new JsonArrayParser<>(ruleParser);
        List<Rule> rules = ruleArrayParser.
                parse(object.getJSONArray("rules"))
                .stream().flatMap(Collection::stream)
                .toList();

        // Parse dictionary
        dictionaryParser = new DictionaryParser(terminals);
        WordDictionary dictionary = dictionaryParser.parse(object.getJSONObject("dictionary"));

        // Parse startPhrase
        Phrase startPhrase = phraseParser.parse(object.getString("start-phrase"));

        return new FormalGrammar(name, vocabulary, rules, dictionary, startPhrase);
    }
}
