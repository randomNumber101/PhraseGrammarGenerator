package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;

import java.util.List;

public class Config {

    private List<Settings> settings;

    private List<FormalGrammar> grammarList;

    private List<GenerationInstance> instances;

    public Config(List<Settings> settings, List<FormalGrammar> grammarList, List<GenerationInstance> instances) {
        this.settings = settings;
        this.grammarList = grammarList;
        this.instances = instances;
    }
    public List<Settings> getSettings() {
        return settings;
    }

    public List<FormalGrammar> getGrammarList() {
        return grammarList;
    }

    public List<GenerationInstance> getInstances() {
        return instances;
    }
}
