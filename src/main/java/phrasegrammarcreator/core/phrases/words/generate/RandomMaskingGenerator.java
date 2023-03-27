package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.Randomizer;

import java.util.List;
import java.util.function.Function;

public class RandomMaskingGenerator extends OutputGenerator{

    public RandomMaskingGenerator(WordGenerationPolicy policy) {
        super(policy);
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
            Randomizer randomizer = Randomizer.getInstance();
            int maskedWord = randomizer.nextInt(parts.size());

            StringBuilder label = new StringBuilder();

            for (int i = 0; i < parts.size(); i++) {
                label.append(i == maskedWord ? "[MASK] " : parts.get(i)).append(" ");
            }
            return label.toString().strip();
        };
    }
}
