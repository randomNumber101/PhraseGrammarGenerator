package phrasegrammarcreator.core.phrases.variables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WordDictionary extends HashMap<Terminal, List<String>> {

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
}
