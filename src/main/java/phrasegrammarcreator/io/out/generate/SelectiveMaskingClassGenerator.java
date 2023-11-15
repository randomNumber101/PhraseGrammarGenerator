package phrasegrammarcreator.io.out.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.phrases.words.WordTerminal;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.*;
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
        this.hasAdditionalStats = true;
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

    private int calculateMaskDepth(EndPhrase endPhrase, int maskPosition) {
        return endPhrase.getNode().getData().get(maskPosition).getDepth();
    }

    private boolean pathContainsNodes(VariableInstance leaf, HashSet nodes) {
        VariableInstance<?> current = leaf;
        while (current != null) {
            current = current.getDerivedFrom();
            if (nodes.contains(current))
                return true;
        }
        return false;
    }

    private int calculateMaskSpan(EndPhrase endPhrase, int maskPosition, int rank) {
        Phrase phrase = endPhrase.getNode().getData();
        VariableInstance masked = phrase.get(maskPosition);

        // Collect parent nodes up to rank
        HashSet<VariableInstance> parentNodes = new HashSet<>();
        for (int i = 0; i <= rank; i++) {
            parentNodes.add(masked.getParent(i));
        }

        int leftSpanEnd = maskPosition;
        int rightSpanEnd = maskPosition;

        // Search left of node
        for (int l = maskedWord - 1; l >= 0; l--) {
            if (pathContainsNodes(phrase.get(l), parentNodes))
                leftSpanEnd = l;
            else
                break;
        }

        // Search right of node
        for (int r = maskedWord + 1; r < phrase.size(); r++) {
            if (pathContainsNodes(phrase.get(r), parentNodes))
                rightSpanEnd = r;
            else
                break;
        }

        return rightSpanEnd - leftSpanEnd;
    }

    private HashMap<String, Integer> getAdvancedStats(EndPhrase endPhrase, int maskedWord) {
        HashMap<String, Integer> stats = new HashMap<>();
        stats.put("depth", calculateMaskDepth(endPhrase, maskedWord));
        for (int rank = 1; rank < 5; rank++) {
            stats.put("r" + rank + "-span", calculateMaskSpan(endPhrase, maskedWord, rank));
        }
        return stats;
    }

    @Override
    protected Function<List<String>, List<Datum>> getAdvancedDatumGenerator(EndPhrase endPhrase, Function<List<String>, String> input, Function<List<String>, String> label) {
        return parts -> List.of(new Datum(
                input.apply(parts),
                label.apply(parts),
                getAdvancedStats(endPhrase, maskedWord)
        ));
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
