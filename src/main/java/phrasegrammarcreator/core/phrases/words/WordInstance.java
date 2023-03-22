package phrasegrammarcreator.core.phrases.words;

import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

public class WordInstance extends VariableInstance<Terminal> {

    public final String value;
    public WordInstance(WordTerminal builder, String value) {
        super(builder);
        this.value = value;
    }

    public String toString() {
        return value;
    }

}
