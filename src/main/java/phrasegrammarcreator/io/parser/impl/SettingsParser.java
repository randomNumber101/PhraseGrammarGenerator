package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.main.Settings;

import java.io.File;
import java.text.ParseException;

public class SettingsParser extends JSonObjectParser<Settings> {

    @Override
    public Settings parse(JSONObject object) throws Exception {

        String outPath = object.getString("output_dir");
        File f = new File(outPath);
        if (f.exists() && f.isDirectory())
            return new Settings("path");
        else
            throw new ParseException("'output-dir' is no valid directory. ", 0);
    }
}
