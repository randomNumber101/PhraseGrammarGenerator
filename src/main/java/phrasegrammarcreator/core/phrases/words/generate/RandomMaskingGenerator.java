package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.util.Randomizer;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class RandomMaskingGenerator extends OutputGenerator{

    public RandomMaskingGenerator(Random random, WordGenerationPolicy policy) {
        super(random, policy);
    }

    @Override
    protected void initialize(EndPhrase endPhrase) {
        return;
    }

    @Override
    protected Function<List<String>, String> getInputGenerator() {
        return parts -> String.join(" ", parts);
    }

    @Override
    protected Function<List<String>, String> getLabelGenerator() {
        return parts -> {
            int maskedWord = random.nextInt(parts.size());

            StringBuilder label = new StringBuilder();

            for (int i = 0; i < parts.size(); i++) {
                label.append(i == maskedWord ? "[MASK] " : parts.get(i)).append(" ");
            }
            return label.toString().strip();
        };
    }
}
