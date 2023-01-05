package phrasegrammarcreator.core.phrases;


import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.List;

public class SubPhrase extends Phrase implements Phrasable{

    Phrase master;
    Occurrence interval;
    public SubPhrase(Phrase master, Occurrence interval) {
        super();
        this.master = master;
        this.interval = interval;
        List<VariableInstance<?>> subList = master.subList(interval.from, interval.to);
        this.addAll(subList);
    }

    @Override
    public Phrase toPhrase() {
        return this;
    }

}
