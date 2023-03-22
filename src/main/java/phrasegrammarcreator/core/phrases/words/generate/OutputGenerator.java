package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.List;

public abstract class OutputGenerator {
    public abstract List<Datum> generate(EndPhrase endPhrase);
}
