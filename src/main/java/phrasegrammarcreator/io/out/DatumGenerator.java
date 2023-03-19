package phrasegrammarcreator.io.out;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.function.Function;

public interface DatumGenerator extends Function<EndPhrase, Datum> {
}
