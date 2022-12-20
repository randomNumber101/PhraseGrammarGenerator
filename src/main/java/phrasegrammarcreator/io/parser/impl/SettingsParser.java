package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.main.Settings;

public class SettingsParser extends JSonObjectParser<Settings> {

    @Override
    public Settings parse(JSONObject object) throws Exception {
        return new Settings();
    }
}
