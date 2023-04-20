package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.io.parser.core.SingleValueParser;
import phrasegrammarcreator.main.GenerationInstance;
import phrasegrammarcreator.main.Settings;

import java.util.HashMap;
import java.util.List;

public class GenerationInstanceParser extends JSonObjectParser<GenerationInstance> {

    HashMap<String, FormalGrammar> grammarByName = new HashMap<>();


    HashMap<String, Settings> settingByName = new HashMap<>();


    public GenerationInstanceParser(List<FormalGrammar> grammars, List<Settings> settings) {
        grammars.forEach(g -> grammarByName.put(g.getName(), g));
        settings.forEach(s -> settingByName.put(s.name(), s));
    }

    @Override
    public GenerationInstance parse(JSONObject object) throws Exception {
        FormalGrammar grammar = grammarByName.get(object.getString("Grammar"));
        if (grammar == null)
            throw new IllegalArgumentException("Grammar not found: " + object.getString("Grammar"));
        Settings setting = settingByName.get(object.getString("Setting"));
        if (setting == null)
            throw new IllegalArgumentException("Setting not found: " + object.getString("Setting"));

        String name = object.getString("file-name");
        boolean doOverwriteFile = object.getBoolean("overwrite-file");

        return new GenerationInstance(grammar, setting, name, doOverwriteFile);
    }
}
