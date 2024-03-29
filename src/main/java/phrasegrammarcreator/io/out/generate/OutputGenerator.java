package phrasegrammarcreator.io.out.generate;

import phrasegrammarcreator.util.IteratorTools;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.words.WordTerminal;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class OutputGenerator implements Function<EndPhrase, List<Datum>> {

    protected WordGenerationPolicy policy;

    protected Random random;

    private final int MAX_NUM_POSSIBILITIES = 4096;

    protected boolean hasAdditionalStats = false;

    public OutputGenerator(Random random, WordGenerationPolicy policy) {
        this.policy = policy;
        this.random = random;
    }

    @Override
    public List<Datum> apply(EndPhrase endPhrase) {

        initialize(endPhrase);

        Function<List<String>, String> input = getInputGenerator();
        Function<List<String>, String> label = getLabelGenerator();
        Function<List<String>, List<Datum>> datum =
                hasAdditionalStats? getAdvancedDatumGenerator(endPhrase, input, label)
                : getDatumGenerator(input, label);


        switch (policy) {
            case SINGLE_RANDOM ->
            {
                List<String> randomWords = endPhrase.stream().map(wt -> wt.getRandomWord(random)).toList();
                return datum.apply(randomWords);
            }
            default -> {
                int[] posCounts = endPhrase.stream().mapToInt(WordTerminal::getWordCount).toArray();
                Iterator<int[]> possibilities = IteratorTools.combinations(posCounts);
                possibilities = IteratorTools.restrictTo(MAX_NUM_POSSIBILITIES, possibilities);

                List<String>[] words = endPhrase.stream().map(WordTerminal::getAllWords).toArray(List[]::new);
                Iterator<List<Datum>> datumIterator =
                        IteratorTools.apply(
                            IteratorTools.getCombination(possibilities, words),
                            datum
                        );

                return IteratorTools.loadAll(datumIterator).stream().flatMap(List::stream).toList();
            }
        }
    }

    /**
     * Basic Datum Generator implementation. Generates one output per Word Combination.
     * Should be overridden when desired.
     * @param input input generator
     * @param label label generator
     * @return a list of data
     */
    protected Function<List<String>, List<Datum>> getDatumGenerator(Function<List<String>, String> input, Function<List<String>, String> label) {
        return parts -> List.of(new Datum(input.apply(parts), label.apply(parts)));
    }

    protected Function<List<String>, List<Datum>> getAdvancedDatumGenerator(EndPhrase endPhrase, Function<List<String>, String> input, Function<List<String>, String> label) {
        HashMap<String, Integer> additionalStats = calculateAdditionalStats(endPhrase);
        return parts -> List.of(new Datum(input.apply(parts), label.apply(parts), additionalStats));
    }

    protected HashMap<String, Integer> calculateAdditionalStats(EndPhrase endPhrase) {
        return null;
    }


    protected abstract void initialize(EndPhrase endPhrase);

    protected abstract Function<List<String>, String> getInputGenerator();

    protected abstract Function<List<String>, String> getLabelGenerator();
}
