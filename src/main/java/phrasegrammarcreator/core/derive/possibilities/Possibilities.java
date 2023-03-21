package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.phrases.Phrase;

import java.util.List;

public abstract class Possibilities implements Iterable<Phrase> {

    public abstract long getCount();

    public abstract void calculateNext();

    public abstract List<Phrase> collectAll();
}
