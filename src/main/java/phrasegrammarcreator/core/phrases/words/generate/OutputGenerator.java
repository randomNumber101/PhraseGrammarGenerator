package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.derive.possibilities.IteratorTools;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.words.WordTerminal;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public abstract class OutputGenerator implements Function<EndPhrase, List<Datum>> {

    public enum WordGenerationPolicy {
        SINGLE_RANDOM,
        ALL_POSSIBILITIES;
    }

    protected WordGenerationPolicy policy;

    public OutputGenerator(WordGenerationPolicy policy) {
        this.policy = policy;
    }

    @Override
    public List<Datum> apply(EndPhrase endPhrase) {

        initialize(endPhrase);

        Function<List<String>, String> input = getInputGenerator();
        Function<List<String>, String> label = getLabelGenerator();
        Function<List<String>, Datum> datum = getDatumGenerator(input, label);


        switch (policy) {
            case SINGLE_RANDOM ->
            {
                List<String> randomWords = endPhrase.stream().map(WordTerminal::getRandomWord).toList();
                return List.of(datum.apply(randomWords));
            }
            default -> {
                int[] posCounts = endPhrase.stream().mapToInt(WordTerminal::getWordCount).toArray();
                Iterator<int[]> possibilities = IteratorTools.combinations(posCounts);
                List<String>[] words = endPhrase.stream().map(WordTerminal::getAllWords).toArray(List[]::new);

                Iterator<Datum> datumIterator =
                        IteratorTools.apply(
                            IteratorTools.getCombination(possibilities, words),
                            datum
                        );

                return IteratorTools.loadAll(datumIterator);
            }
        }
    }

    protected Function<List<String>, Datum> getDatumGenerator(Function<List<String>, String> input, Function<List<String>, String> label) {
        return parts -> new Datum(input.apply(parts), label.apply(parts));
    }


    protected abstract void initialize(EndPhrase endPhrase);

    protected abstract Function<List<String>, String> getInputGenerator();

    protected abstract Function<List<String>, String> getLabelGenerator();
}
