package phrasegrammarcreator.compute;

public class SingleOccurrence extends Occurrence {
    public SingleOccurrence(int index) {
        super(index, index + 1);
    }
}
