package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;

public class State {

    public enum STATES {
        LOAD_CONFIG,
        CHOOSE_GRAMMAR,
        LOADED;
    }

    static State current;
    private STATES state;
    private Config config;
    private FormalGrammar configured;

    private State() {
        updateState();
    }

    private void updateState() {
        if (config == null)
            state = STATES.LOAD_CONFIG;
        else if (configured == null)
            state = STATES.CHOOSE_GRAMMAR;
        else
            state = STATES.LOADED;
    }

    public FormalGrammar getConfigured() {
        return configured;
    }

    public void setConfigured(FormalGrammar configured) {
        this.configured = configured;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
