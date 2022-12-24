package phrasegrammarcreator.io.print;

import java.util.Collection;
import java.util.List;

public class Util {

    public static String LIST_BULLET_POINT = "*";
    public static enum ALIGN {LEFT, MID, RIGHT}

    public static <T> String formatCollection(String header, Collection<T> c, PrintTransformer<T>  transformer, boolean numbered) {
        StringBuilder out = header != null ? new StringBuilder(header + "\n") : new StringBuilder();
        if (!numbered) {
            for (T t : c) {
                String newLine = String.format("\t%s %s\n", LIST_BULLET_POINT, transformer.print(t));
                out.append(newLine);
            }
        }
        else {
            int index = 0;
            for (T t : c) {
                String newLine = String.format("\t[%d] %s\n", index++, transformer.print(t));
                out.append(newLine);
            }
        }

        out.delete(out.lastIndexOf("\n"), out.length());
        return out.toString();
    }
    public static <T> String formatCollection(String header, Collection<T> c, PrintTransformer<T>  transformer) {
        return formatCollection(header, c, transformer, false);
    }
    public static <T> String formatCollection(Collection<T> c, PrintTransformer<T>  transformer) {
        return formatCollection(null, c, transformer);
    }

    public static List<String> padList (List<String> list, ALIGN alignment) {
        return padList(list, alignment, ' ');
    }
    public static List<String> padList (List<String> list, ALIGN alignment, char padder) {
        int maxLength = maxLength(list);
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            String paddedS = pad(s, maxLength, alignment, padder);
            list.set(i, paddedS);
        }
        return list;
    }

    public static String padLeft(String s, int size) {
        return pad(s, size, ALIGN.LEFT, ' ');
    }
    public static String padMid(String s, int size) {
        return pad(s, size, ALIGN.MID, ' ');
    }
    public static String pad(String s, int size, ALIGN alignment, char padder) {
        String pad = String.valueOf(padder);
        int diff = size - s.length();
        int half = diff / 2;
        String paddedS = switch (alignment){
            case LEFT -> s + pad.repeat(diff);
            case RIGHT -> pad.repeat(diff) + s;
            case MID -> pad.repeat(half) + s + pad.repeat(diff - half);
        };
        return paddedS;
    }

    public static int maxLength(List<String> list) {
        final int[] maxLength = {0};
        list.stream().forEach(s -> {
                    if (s.length() > maxLength[0]) {
                        maxLength[0] = s.length();}
                }
        );
        return maxLength[0];
    }



}
