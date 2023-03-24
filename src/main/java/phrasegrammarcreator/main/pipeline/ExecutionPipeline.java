package phrasegrammarcreator.main.pipeline;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.GenerationInstance;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class ExecutionPipeline extends AbstractPipe<GenerationInstance, DataSet> {

    FormalGrammar grammar;

    ForkPipe<GenerationInstance, Phrase> possibilityPipe;

    ParallelPipe<Phrase, EndPhrase> derivationPipe;

    ParallelPipe<EndPhrase, Datum> dataPipe;

    JoinPipe<Datum, DataSet> dataMergePipe;

    public ExecutionPipeline(FormalGrammar grammar) {
        this.grammar = grammar;
    }


    @Override
    public DataSet apply(GenerationInstance generationInstance) {
        return possibilityPipe
                .andThen(derivationPipe)
                .andThen(dataPipe)
                .andThen(dataMergePipe)
                .apply(generationInstance);
    }
}
