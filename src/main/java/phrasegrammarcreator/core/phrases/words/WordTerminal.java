package phrasegrammarcreator.core.phrases.words;

import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.main.Randomizer;

import java.util.List;
import java.util.Random;

public class WordTerminal extends Terminal {

    private final WordDictionary wordDictionary;
    private final Terminal parent;
    Random random = Randomizer.getInstance();

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

    public List<String> getAllWords() {
        return wordDictionary.get(parent);
    }

    @Override
    public VariableInstance<Terminal> createInstance() {
        return new WordInstance(this, getRandomWord());
    }

    public Terminal getParent() {
        return parent;
    }

}
