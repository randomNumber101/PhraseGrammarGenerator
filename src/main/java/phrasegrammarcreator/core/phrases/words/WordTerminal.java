package phrasegrammarcreator.core.phrases.words;

import phrasegrammarcreator.core.phrases.variables.Terminal;

import java.util.List;
import java.util.Random;

public class WordTerminal {
    private final WordDictionary wordDictionary;
    private final Terminal parent;

    public WordTerminal(WordDictionary wordDictionary, Terminal parent) {
        this.parent = parent;
        this.wordDictionary = wordDictionary;
    }

    public String getRandomWord(Random random) {
        List<String> options = wordDictionary.get(parent);
        return options.get(random.nextInt(options.size()));
    }

    public List<String> getAllWords() {
        return wordDictionary.get(parent);
    }

    public int getWordCount() {
        return wordDictionary.get(parent).size();
    }

    public Terminal getTerminal() {
        return parent;
    }
}
