package phrasegrammarcreator.io.print;

import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Visual {
    private static final String[] SUPERSCRIPT_NUM = {
            "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"};

    public static String getSuperscriptNumber(int n) {
        if (n < 10)
            return SUPERSCRIPT_NUM[n];
        else {
            int lastDigit = n % 10;
            int shiftedRight = (n - lastDigit) / 10;
            return getSuperscriptNumber(shiftedRight) + SUPERSCRIPT_NUM[lastDigit];
        }
    }

    public static String markOccurrence(Phrase phrase, Occurence occurence) {
        return markOccurrences(phrase, occurence);
    }

    /**
     * Marks occurences in phrases.
     * E.g.: ABJRIWQSD -> AB [JRI]⁰ WQS [D]¹
     *
     * @param phrase  The phrase
     * @param occurrences The intervals to mark
     * @return String of format like: AB [JRI]⁰ WQS [D]¹
     */
    public static String markOccurrences(Phrase phrase, Occurence... occurrences) {
        Set<Integer> froms = Arrays.stream(occurrences).map(o -> o.from).collect(Collectors.toSet());
        Set<Integer> tos = Arrays.stream(occurrences).map(o -> o.to).collect(Collectors.toSet());
        ArrayList<String> subStrings = phrase.stream()
                .map(VariableInstance::toString)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i <= phrase.size(); i++) {
            if (froms.contains(i)) {
                subStrings.set(i, " [" + subStrings.get(i));
            }
            if (tos.contains(i)) {
                subStrings.set(i - 1, subStrings.get(i - 1) + "] ");
            }
        }
        StringBuilder builder = new StringBuilder();
        int closedBracketCount = 0;
        for (String s : subStrings) {
            if (s.startsWith("]")) {
                if (occurrences.length > 1)
                    s = s.replace("]", "]" + getSuperscriptNumber(closedBracketCount++));
            }
            builder.append(s);
        }
        String out = builder.toString();
        return out.replaceAll(" +", " ");
    }

}


