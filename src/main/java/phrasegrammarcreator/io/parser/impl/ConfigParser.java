package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.JsonArrayParser;
import phrasegrammarcreator.main.Config;
import phrasegrammarcreator.main.GenerationInstance;
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

        JsonArrayParser<JSONObject, Settings> settingsArrayParser;
        settingsArrayParser = new JsonArrayParser<>(settingsParser);
        List<Settings> settings = settingsArrayParser.parse(object.getJSONArray("Settings"));



        JSonObjectParser<GenerationInstance> instanceParser = new JSonObjectParser<>() {
            @Override
            public GenerationInstance parse(JSONObject object) throws Exception {
                FormalGrammar grammar = grammarList.get(object.getInt("Grammar"));
                Settings setting = settings.get(object.getInt("Setting"));

                return new GenerationInstance(grammar, setting);
            }
        };
        JsonArrayParser<JSONObject, GenerationInstance> instanceArrayParser;
        instanceArrayParser = new JsonArrayParser<>(instanceParser);
        List<GenerationInstance> instances = instanceArrayParser.parse(object.getJSONArray("Outputs"));

        return new Config(settings, grammarList, instances);
    }
}
