package phrasegrammarcreator.core.phrases.words.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.List;
import java.util.function.Function;

public abstract class OutputGenerator implements Function<EndPhrase, List<Datum>> {
    public abstract List<Datum> generate(EndPhrase endPhrase);

    @Override
    public List<Datum> apply(EndPhrase endPhrase) {
        return generate(endPhrase);
    }
}
