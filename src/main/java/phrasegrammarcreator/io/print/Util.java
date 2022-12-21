package phrasegrammarcreator.io.print;

import java.util.Collection;

public class Util {

    public static String LIST_BULLET_POINT = "*";

    public static <T> String printCollection(String header, Collection<T> c, PrintTransformer<T>  transformer, boolean numbered) {
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
    public static <T> String printCollection(String header, Collection<T> c, PrintTransformer<T>  transformer) {
        return printCollection(header, c, transformer, false);
    }
    public static <T> String printCollection(Collection<T> c, PrintTransformer<T>  transformer) {
        return printCollection(null, c, transformer);
    }

}
