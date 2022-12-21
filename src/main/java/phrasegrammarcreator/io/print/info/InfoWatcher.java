package phrasegrammarcreator.io.print.info;

import java.io.PrintStream;

public abstract class InfoWatcher<T> {

    PrintStream out;
    T watched;

    public InfoWatcher(PrintStream out, T watched) {
        this.out = out;
        this.watched = watched;
    }
    public abstract String toString(T watched);

    public void printInfo() {
        out.println(toString(watched));
    }
}
