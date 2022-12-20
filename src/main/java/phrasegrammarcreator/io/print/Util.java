package phrasegrammarcreator.io.print;

import java.util.Collection;

public class Util {

    public static String LIST_BULLET_POINT = "+";

    public static <T> String printCollection(String header, Collection<T> c, PrintTransformer<T>  transformer) {
        StringBuilder out = header != null ? new StringBuilder(header + "\n") : new StringBuilder();
        for (T t : c) {
            String newLine = String.format("\t%s %s\n", LIST_BULLET_POINT, transformer.print(t));
            out.append(newLine);
        }
        return out.toString();
    }
    public static <T> String printCollection(Collection<T> c, PrintTransformer<T>  transformer) {
        return printCollection(null, c, transformer);
    }

}
