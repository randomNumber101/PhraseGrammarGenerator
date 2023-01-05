package phrasegrammarcreator.core.phrases.variables.words;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.List;
import java.util.Random;

public class WordTerminal extends Terminal {

    private final WordDictionary wordDictionary;
    private final Terminal parent;
    Random random = new Random();

    public WordTerminal(WordDictionary wordDictionary, Terminal parent) {
        super(parent.getRegex(), parent.getName());
        this.parent = parent;
        this.wordDictionary = wordDictionary;
    }
    public Type getType() {
        return Type.WORD;
    }

    public String getRandomWord() {
        List<String> options = wordDictionary.get(parent);
        return options.get(random.nextInt(options.size()));
    }

    @Override
    public VariableInstance<Terminal> createInstance(Phrase phrase) {
        return new WordInstance(this, phrase, getRandomWord());
    }

    public Terminal getParent() {
        return parent;
    }



}
