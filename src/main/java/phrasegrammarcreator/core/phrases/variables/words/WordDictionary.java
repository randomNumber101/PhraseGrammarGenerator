package phrasegrammarcreator.core.phrases.variables.words;

import phrasegrammarcreator.core.phrases.variables.Terminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WordDictionary extends HashMap<Terminal, List<String>> {


    private final HashMap<Terminal, WordTerminal> wordTerminalHashMap = new HashMap<>();
    private List<Terminal> terminals;

    public WordDictionary(List<Terminal> allTerminals) {
        super();
        this.terminals = allTerminals;
        for (Terminal t : terminals) {
            this.put(t, new ArrayList<>());
        }
    }

    public boolean add(Terminal terminal, String word) {
        if (!containsKey(terminal))
            return false;
        get(terminal).add(word);
        return true;
    }

    public boolean add(Terminal terminal, Collection<String> words) {
        if (!containsKey(terminal))
            return false;
        get(terminal).addAll(words);
        return true;
    }

    public List<Terminal> getEmptyEntries() {
        return terminals.stream().filter(t -> get(t).isEmpty()).toList();
    }

    public WordTerminal toWord(Terminal terminal) {
        if(!wordTerminalHashMap.containsKey(terminal) || wordTerminalHashMap.get(terminal) == null) {
            wordTerminalHashMap.put(terminal, new WordTerminal(this, terminal));
        }
        return wordTerminalHashMap.get(terminal);
    }
}
