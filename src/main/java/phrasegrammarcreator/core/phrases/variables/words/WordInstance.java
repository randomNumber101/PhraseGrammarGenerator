package phrasegrammarcreator.core.phrases.variables.words;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

public class WordInstance extends VariableInstance<Terminal> {

    public final String value;
    public WordInstance(WordTerminal builder, Phrase phrase, String value) {
        super(builder, phrase);
        this.value = value;
    }

    public String toString() {
        return value;
    }

}
