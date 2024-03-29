package phrasegrammarcreator.io.parser.impl;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.JsonArrayParser;
import phrasegrammarcreator.io.parser.core.SingleValueParser;
import phrasegrammarcreator.io.parser.impl.external.CFGLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrammarObjectParser extends JSonObjectParser<FormalGrammar> {

    Logger logger = Logger.getLogger(GrammarObjectParser.class.getName());

    private SingleValueParser<NonTerminal> nonTerminalParser;
    private SingleValueParser<Terminal> terminalParser;
    private SingleValueParser<List<Rule>> ruleParser;
    private PhraseParser phraseParser;
    private DictionaryParser dictionaryParser;

    private Vocabulary vocabulary;

    public GrammarObjectParser() {
        innit();
    }

    private void innit() {
        // Create new vocabulary for each parsing
        vocabulary = new Vocabulary();
        nonTerminalParser = new NonTerminalParser(vocabulary);
        terminalParser = new TerminalParser(vocabulary);
        ruleParser = new RuleParser(vocabulary);
        phraseParser = new PhraseParser(vocabulary);
    }

    @Override
    public FormalGrammar parse(JSONObject object) throws Exception {
        innit();

        // Parse name
        String name = object.getString("name");

        // If this is an external CFG file. Use CFG Parser instead
        if (object.has("load-from")) {
            String path = object.getString("load-from");
            File file = new File(path);

            String extension = path.substring(path.lastIndexOf('.') + 1);
            switch (extension) {
                case "cfg" -> {
                    return new CFGLoader(file, name).load();
                }
                default -> throw new IllegalArgumentException("Can't load grammar from "+ path);
            }

        }

        // Parse Non-Terminals, Terminals (adding them to the vocabulary)
        JsonArrayParser<String, NonTerminal> ntArrayParser = new JsonArrayParser<>(nonTerminalParser);
        List<NonTerminal> nonTerminals = ntArrayParser.parse(object.getJSONArray("non-terminals"));
        JsonArrayParser<String, Terminal> tArrayParser = new JsonArrayParser<>(terminalParser);
        List<Terminal> terminals = tArrayParser.parse(object.getJSONArray("terminals"));

        // Get mask-worthy Terminals (Terminals that should be masked, default : all)
        List<Terminal> maskWorthy;
        if (object.has("mask-worthy")) {
            maskWorthy = tArrayParser.parse(object.getJSONArray("mask-worthy"));
        }
        else {
            // default: all
            maskWorthy = new ArrayList<>(terminals);
        }
        vocabulary.setMaskWorthy(maskWorthy);

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

        List<Terminal> noWordsTerminals = dictionary.getEmptyEntries();
        if (!noWordsTerminals.isEmpty())
            logger.warn(
                    String.format("""
                                    Some Terminals don't have words in the dictionary:\s
                                    %s\s
                                    Using their names for derivation instead.\s""",
                            noWordsTerminals)
            );

        return new FormalGrammar(name, vocabulary, rules, dictionary, startPhrase);
    }

}
