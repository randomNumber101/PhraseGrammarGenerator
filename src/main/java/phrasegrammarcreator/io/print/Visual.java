package phrasegrammarcreator.io.print;

import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.List;
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
        return markOccurrences(phrase, List.of(occurence));
    }

    /**
     * Marks occurences in phrases.
     * E.g.: ABJRIWQSD -> AB [JRI]⁰ WQS [D]¹
     *
     * @param phrase  The phrase
     * @param occurrences The intervals to mark
     * @return String of format like: AB [JRI]⁰ WQS [D]¹
     */
    public static String markOccurrences(Phrase phrase, List<Occurence> occurrences) {
        Set<Integer> froms = occurrences.stream().map(o -> o.from).collect(Collectors.toSet());
        Set<Integer> tos = occurrences.stream().map(o -> o.to).collect(Collectors.toSet());
        List<String> subStrings = phrase.stream()
                .map(VariableInstance::toString)
                .toList();

        for (int i = 0; i < phrase.size(); i++) {
            if (froms.contains(i)) {
                subStrings.set(i, " [" + subStrings.get(i));
            }
            if (tos.contains(i)) {
                subStrings.set(i, "] " + subStrings.get(i));
            }
        }
        StringBuilder builder = new StringBuilder();
        int closedBracketCount = 0;
        for (String s : subStrings) {
            if (s.startsWith("]")) {
                s = s.replace("]", "]" + getSuperscriptNumber(closedBracketCount++));
            }
            builder.append(s);
        }
        String out = builder.toString();
        return out.replaceAll("  ", " ");
    }

}


