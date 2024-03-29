package phrasegrammarcreator.io.out.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.words.WordTerminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SelectiveMaskingGenerator extends OutputGenerator {

    private int maskedWord = -1;

    private List<Integer> maskWorthyWords;

    /**
     * Masks one random mask worthy word, label is the word
     * @param random Randomizer
     * @param policy WordGenerationPolicy
     */
    public SelectiveMaskingGenerator(Random random, WordGenerationPolicy policy) {
        super(random, policy);
    }

    @Override
    protected void initialize(EndPhrase endPhrase) {
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

            return parts.get(maskedWord);
        };
    }
}
