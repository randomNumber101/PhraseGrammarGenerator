package phrasegrammarcreator.io.print;

import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static String markOccurrence(Phrase phrase, Occurrence occurrence) {
        return markOccurrences(phrase, occurrence);
    }

    /**
     * Marks occurences in phrases.
     * E.g.: ABJRIWQSD -> AB [JRI]⁰ WQS [D]¹
     *
     * @param phrase  The phrase
     * @param occurrences The intervals to mark
     * @return String of format like: AB [JRI]⁰ WQS [D]¹
     */
    public static String markOccurrences(Phrase phrase, Occurrence... occurrences) {
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

    public static List<String> printTable(List<String> header, List<List<String>> columns) {
        ArrayList<Integer> widths = columns.stream().map(Util::maxLength).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < widths.size(); i++) {
            widths.set(i, Math.max(widths.get(i), header.get(i).length()));
        }

        int width = columns.size();
        int height = columns.isEmpty()? 0 : columns.get(0).size();

        ArrayList<String> out = new ArrayList<>();
        StringBuilder headerBuilder = new StringBuilder("   ");
        for (int i = 0; i < widths.size(); i++) {
            headerBuilder.append(Util.padMid(header.get(i), widths.get(i))).append("   ");
        }
        out.add(headerBuilder.toString());

        for (int i = 0; i < height; i++) {
            StringBuilder rowBuilder = new StringBuilder("║ ");
            for (int j = 0; j < width; j++) {
                String entry = columns.get(j).get(i);
                rowBuilder.append(Util.padMid(entry, widths.get(j))).append(" ║ ");
            }
            out.add(rowBuilder.toString());
        }

        return out;
    }

}


