package phrasegrammarcreator.core.phrases;


import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

import java.util.List;

public class SubPhrase extends Phrase implements Phrasable{

    Phrase master;
    Occurence interval;
    public SubPhrase(Phrase master, Occurence interval) {
        super();
        this.master = master;
        this.interval = interval;
        List<VariableInstance> subList = master.subList(interval.from, interval.to);
        this.addAll(subList);
    }

    @Override
    public Phrase toPhrase() {
        return null;
    }

    @Override
    public VariableInstance get(int i) {
        if (i >= interval.to)
            return null;
        return master.get(i + interval.from);
    }

}
