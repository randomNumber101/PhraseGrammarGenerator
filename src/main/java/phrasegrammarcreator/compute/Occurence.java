package phrasegrammarcreator.compute;

import org.jetbrains.annotations.NotNull;

public class Occurence implements Comparable<Occurence>{
    public int from;
    public int to;

    public Occurence(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public Occurence shiftedBy(int shift) {
        return new Occurence(from + shift, to + shift);
    }

    @Override
    public int compareTo(@NotNull Occurence o) {
        // Sort by interval start
        return Integer.compare(this.from, o.from);
    }
}
