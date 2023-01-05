package phrasegrammarcreator.compute.pick.words;

import phrasegrammarcreator.core.phrases.variables.words.WordTerminal;

public class RandomWordGenerator extends WordGenerator<WordTerminal>{
    @Override
    public String nextString(WordTerminal variable) {
        return variable.getRandomWord();
    }
}
