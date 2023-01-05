package phrasegrammarcreator.compute.pick.words;

import phrasegrammarcreator.core.phrases.variables.Variable;

public abstract class WordGenerator<T extends Variable> {


    public abstract String nextString(T variable);
}
