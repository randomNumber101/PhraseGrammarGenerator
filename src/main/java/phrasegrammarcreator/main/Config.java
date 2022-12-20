package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;

import java.util.List;

public class Config {

    private Settings settings;

    private List<FormalGrammar> grammarList;

    public Config(Settings settings, List<FormalGrammar> grammarList) {
        this.settings = settings;
        this.grammarList = grammarList;
    }

    public Settings getSettings() {
        return settings;
    }

    public List<FormalGrammar> getGrammarList() {
        return grammarList;
    }
}
