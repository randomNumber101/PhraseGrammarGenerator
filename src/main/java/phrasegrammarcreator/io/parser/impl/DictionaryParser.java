package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.words.WordDictionary;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.JsonArrayParser;
import phrasegrammarcreator.io.parser.core.StringParser;

import java.util.List;

public class DictionaryParser extends JSonObjectParser<WordDictionary> {

    private List<Terminal> terminals;

    private JsonArrayParser<String, String> wordsParser;

    public DictionaryParser(List<Terminal> terminals) {
        this.terminals = terminals;
        wordsParser = new JsonArrayParser<>(new StringParser());
    }


    @Override
    public WordDictionary parse(JSONObject object) throws Exception {
        WordDictionary dictionary = new WordDictionary(terminals);
        for (Terminal t : terminals) {
            if (object.has(t.toString())) {
                dictionary.add(t, wordsParser.parse(object.getJSONArray(t.getRegex())));
            }
            else
                dictionary.add(t, t.getName());
        }
        return dictionary;
    }
}
