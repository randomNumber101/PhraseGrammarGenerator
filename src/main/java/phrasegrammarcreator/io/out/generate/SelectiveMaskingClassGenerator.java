package phrasegrammarcreator.io.out.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.words.WordTerminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SelectiveMaskingClassGenerator extends OutputGenerator {

    private int maskedWord = -1;

    private List<Integer> maskWorthyWords;

    private List<Variable> terminals;

    /**
     * Masks one random mask worthy word, label is the class (variable) of the word
     * @param random Randomizer
     * @param policy WordGenerationPolicy
     */
    public SelectiveMaskingClassGenerator(Random random, WordGenerationPolicy policy) {
        super(random, policy);
    }

    @Override
    protected void initialize(EndPhrase endPhrase) {
        terminals = endPhrase.getNode().getData().transformToBuilders();
        maskWorthyWords = new ArrayList<>();
        for (int i = 0; i < endPhrase.size(); i++) {
            WordTerminal wt = endPhrase.get(i);
            if (wt.getTerminal().isMaskWorthy())
                maskWorthyWords.add(i);
        }
    }



    @Override
    protected Function<List<String>, String> getInputGenerator() {
        return parts -> {
            if (maskWorthyWords.isEmpty())
                maskedWord = -1;
            else
                maskedWord = maskWorthyWords.get(random.nextInt(maskWorthyWords.size()));

            StringBuilder label = new StringBuilder();

            for (int i = 0; i < parts.size(); i++) {
                label.append(i == maskedWord ? "[MASK]" : parts.get(i)).append(" ");
            }
            return label.toString().strip();
        };
    }

    @Override
    protected Function<List<String>, String> getLabelGenerator() {
        return parts -> {
            if (maskWorthyWords.isEmpty())
                return "[NONE]";

            return terminals.get(maskedWord).toString();
        };
    }
}
