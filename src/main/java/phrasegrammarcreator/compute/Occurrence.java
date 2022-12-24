package phrasegrammarcreator.compute;

import org.jetbrains.annotations.NotNull;

public class Occurrence implements Comparable<Occurrence>{
    public int from;
    public int to;

    public Occurrence(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public Occurrence shiftedBy(int shift) {
        return new Occurrence(from + shift, to + shift);
    }

    public Occurrence extendedBy(int extension) {
        return new Occurrence(from, to + extension);
    }

    @Override
    public int compareTo(@NotNull Occurrence o) {
        // Sort by interval start
        return Integer.compare(this.from, o.from);
    }
    @Override
    public String toString() {
        return String.format("(%d,%d)", from, to);
    }
}
