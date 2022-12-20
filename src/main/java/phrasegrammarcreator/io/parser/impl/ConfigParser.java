package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.JsonArrayParser;
import phrasegrammarcreator.main.Config;
import phrasegrammarcreator.main.Settings;

import java.util.List;

public class ConfigParser extends JSonObjectParser<Config> {

    private JSonObjectParser<FormalGrammar> grammarParser;
    private JSonObjectParser<Settings> settingsParser;

    public ConfigParser() {
        grammarParser = new GrammarObjectParser();
        settingsParser = new SettingsParser();
    }

    @Override
    public Config parse(JSONObject object) throws Exception {

        JsonArrayParser<JSONObject, FormalGrammar> grammarArrayParser;
        grammarArrayParser = new JsonArrayParser<>(grammarParser);
        List<FormalGrammar> grammarList = grammarArrayParser.parse(object.getJSONArray("Grammars"));

        Settings settings = settingsParser.parse(object.getJSONObject("Settings"));

        return new Config(settings, grammarList);
    }
}
