package phrasegrammarcreator.compute.pick.words;

import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.words.WordTerminal;

public class MixedWordGenerator extends WordGenerator<Variable> {

    private WordGenerator<WordTerminal> forWords;
    private WordGenerator<Variable> orElse;

    public MixedWordGenerator(WordGenerator<WordTerminal> forWords, WordGenerator<Variable> orElse) {
        this.forWords = forWords;
        this.orElse = orElse;
    }

    public MixedWordGenerator(WordGenerator<WordTerminal> forWords) {
        this(forWords, new VariableNameWordGenerator());
    }

    @Override
    public String nextString(Variable variable) {
        if (variable instanceof WordTerminal) {
            return forWords.nextString((WordTerminal) variable);
        }
        return orElse.nextString(variable);
    }
}
