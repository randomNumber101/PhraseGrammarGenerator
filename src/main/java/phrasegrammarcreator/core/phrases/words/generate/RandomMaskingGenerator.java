package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.Randomizer;

import java.util.List;

public class RandomMaskingGenerator extends OutputGenerator{
    @Override
    public List<Datum> generate(EndPhrase endPhrase) {

        Randomizer randomizer = Randomizer.getInstance();
        int maskedWord = randomizer.nextInt(endPhrase.size());

        String label = "";
        String input = "";

        for (int i = 0; i < endPhrase.size(); i++) {
            label += endPhrase.get(0).getRandomWord() + " ";
            input += (i == maskedWord? "[MASK]" : endPhrase.get(0).getRandomWord()) + " ";
        }

        return List.of(new Datum(input, label));
    }
}
