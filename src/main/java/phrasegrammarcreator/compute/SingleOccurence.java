package phrasegrammarcreator.compute;

public class SingleOccurence extends Occurence{
    public SingleOccurence(int index) {
        super(index, index + 1);
    }
}
