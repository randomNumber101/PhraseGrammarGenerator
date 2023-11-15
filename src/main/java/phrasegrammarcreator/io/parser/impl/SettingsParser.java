package phrasegrammarcreator.io.parser.impl;

import org.json.JSONObject;
import phrasegrammarcreator.io.out.generate.Task;
import phrasegrammarcreator.io.out.generate.WordGenerationPolicy;
import phrasegrammarcreator.io.parser.core.JSonObjectParser;
import phrasegrammarcreator.main.Settings;

import java.io.File;
import java.text.ParseException;

public class SettingsParser extends JSonObjectParser<Settings> {

    @Override
    public Settings parse(JSONObject object) throws Exception {

        String name = object.getString("name");

        Long seed = object.has("seed")? object.getLong("seed") : null;

        String outPath = object.getString("output-dir");
        File f = new File(outPath);
        if (f.exists() &! f.isDirectory())
            throw new ParseException("'output-dir' is no valid directory. ", 0);

        WordGenerationPolicy policy = WordGenerationPolicy.parse(object.getString("word-generation"));
        Task task = Task.parse(object.getString("task"));


        int possibilityCap = object.getInt("phrase-count-cap");
        int depthCap = object.getInt("search-tree-depth-cap");
        int lengthCap = object.getInt("phrase-length-cap");

        // TODO: Add adjustable strategy:
        // strategy = parseStrategy(object.getString("derivation-pick-strategy"));

        return new Settings(name, seed, outPath, policy, task, possibilityCap, depthCap, lengthCap);
    }
}
