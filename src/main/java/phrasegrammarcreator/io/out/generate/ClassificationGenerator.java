package phrasegrammarcreator.io.out.generate;

import org.apache.commons.lang3.NotImplementedException;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.parser.CYKParser;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ClassificationGenerator extends OutputGenerator{

    static final float swapPercentage = 0.25f;
    static final float insertPercentage = 0.25f;
    static final float deletePercentage = 0.1f;

    static final String TRUE_LABEL = "TRUE";
    static final String FALSE_LABEL = "FALSE";

    private static HashMap<FormalGrammar, CYKParser> parsers = new HashMap<>();

    private static CYKParser getParser(FormalGrammar grammar) {
        if (!parsers.containsKey(grammar)) {
            CYKParser parser = new CYKParser(grammar);
            parsers.put(grammar, parser);
        }
        return parsers.get(grammar);
    }

    CfRuleContainer container;
    CYKParser parser;

    private List<FalsificationStrategy> falsificationStrategies;
    public ClassificationGenerator(Random random, WordGenerationPolicy policy, CfRuleContainer container, FormalGrammar grammar) {
        super(random, policy);
        this.container = container;
        this.parser = getParser(grammar);
        falsificationStrategies = List.of(new SwapParts(random), new InsertParts(random), new RemoveParts(random));
    }

    @Override
    protected Function<List<String>, List<Datum>> getDatumGenerator(Function<List<String>, String> input, Function<List<String>, String> label) {
        // Input and Label generators are not needed here
        return words -> {
            try {
                return List.of(
                        new Datum(String.join(" ", words), TRUE_LABEL),
                        new Datum(String.join(" ", falsify(words)), FALSE_LABEL));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public void initialize(EndPhrase ep) {
    }

    @Override
    protected Function<List<String>, String> getInputGenerator() {
        return null;
    }

    @Override
    protected Function<List<String>, String> getLabelGenerator() {
        return null;
    }

    private List<String> falsify(List<String> parts) throws Exception {
        int attempts = 0;
        do {
            for (FalsificationStrategy strategy : falsificationStrategies) {
                parts = strategy.apply(parts);
            }
            attempts++;
        }
        while (attempts < 3 && parser.parse(String.join(" ", parts)));
        return parts;
    }



    private abstract class FalsificationStrategy implements Function<List<String>, List<String>> {};
    private class RemoveParts extends FalsificationStrategy {
        Random random;
        public RemoveParts(Random random) {
            this.random = random;
        }
        @Override
        public List<String> apply(List<String> strings) {
            strings = new ArrayList<>(strings);
            int removeCount = (int) (deletePercentage * strings.size());
            if (removeCount == 0 && strings.size() > 0)
                removeCount = 1;

            for (int i = 0; i < removeCount; i++) {
                strings.remove(random.nextInt(strings.size()));
            }

            return strings;
        }
    }
    private class SwapParts extends FalsificationStrategy {
        Random random;
        public SwapParts(Random random) {
            this.random = random;
        }
        @Override
        public List<String> apply(List<String> strings) {
            strings = new ArrayList<>(strings);
            int swapCount = (int) (swapPercentage * strings.size()); // Swap rate: 25%
            if (swapCount == 0 && strings.size() > 1)
                swapCount = 1;

            for (int i = 0; i < swapCount; i++) {
                int a = random.nextInt(strings.size());
                int b = random.nextInt(strings.size());

                String tmp = strings.get(a);
                strings.set(a, strings.get(b));
                strings.set(b, tmp);
            }

            return strings;
        }
    }
    private class InsertParts extends FalsificationStrategy {
        Random random;
        public InsertParts(Random random) {
            this.random = random;
        }
        @Override
        public List<String> apply(List<String> strings) {
            strings = new ArrayList<>(strings);
            int insertCount = (int) (insertPercentage * strings.size());
            if (insertCount == 0 && strings.size() > 0)
                insertCount = 1;

            for (int i = 0; i < insertCount; i++) {
                int copy = random.nextInt(strings.size());
                int place = random.nextInt(strings.size());
                strings.add(place, strings.get(copy));
            }

            return strings;
        }
    }


}
