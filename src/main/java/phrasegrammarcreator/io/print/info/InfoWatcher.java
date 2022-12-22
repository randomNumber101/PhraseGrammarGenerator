package phrasegrammarcreator.io.print.info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public abstract class InfoWatcher<T> {

    PrintStream out;
    T watched;

    public InfoWatcher(PrintStream out, T watched) {
        this.out = out;
        this.watched = watched;
    }
    public abstract String toString(T watched);

    protected String printToString(Runnable... printCommands) {
        String printed = "";
        PrintStream previous = out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();
        try (PrintStream ps = new PrintStream(baos, true, utf8)) {
            out = ps;
            for (Runnable printCommand : printCommands) {
                printCommand.run();
            }
            printed = baos.toString(utf8);
            baos.close();
            return printed;
        } catch (IOException e) {
            e.printStackTrace(out);
            throw new RuntimeException(e);
        } finally {
            out = previous;
        }
    }

    public void printInfo() {
        out.println(toString(watched));
    }
}
