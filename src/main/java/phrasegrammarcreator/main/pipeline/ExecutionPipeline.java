package phrasegrammarcreator.main.pipeline;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.compute.calculate.ContextFreeCalculator;
import phrasegrammarcreator.compute.pick.derivation.SmartChooser;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.PossibilitiesGenerator;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.EndPhraseGenerator;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.words.generate.OutputGenerator;
import phrasegrammarcreator.core.phrases.words.generate.RandomMaskingGenerator;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.io.out.jsonObjects.MetaInformation;
import phrasegrammarcreator.main.GenerationInstance;
import phrasegrammarcreator.main.Randomizer;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ExecutionPipeline extends AbstractPipe<GenerationInstance, DataSet> {

    FormalGrammar grammar;

    ForkPipe<GenerationInstance, Phrase> possibilityPipe;

    ParallelPipe<Phrase, EndPhrase> derivationPipe;

    ParallelPipe<EndPhrase, Datum> dataPipe;

    JoinPipe<Datum, DataSet> dataMergePipe;

    public ExecutionPipeline(FormalGrammar grammar) {
        this.grammar = grammar;

        PossibilitiesGenerator pg = new PossibilitiesGenerator(grammar, grammar.getStartPhrase());
        possibilityPipe = new ForkPipe<>(pg);

        ContextFreeCalculator calculator = new ContextFreeCalculator(grammar.getRuleContainer());
        SmartChooser chooser = new SmartChooser(grammar);
        EndPhraseGenerator endPhraseGenerator = new EndPhraseGenerator(grammar, calculator, chooser);
        derivationPipe = new ParallelPipe<>(endPhraseGenerator);

        OutputGenerator datumGenerator = new RandomMaskingGenerator();
        dataPipe = new ParallelPipe<>(new ForkPipe<>(datumGenerator.andThen(List::iterator)));

        dataMergePipe = new JoinPipe<>(datumIterator -> {
            MetaInformation metaInformation =
                    new MetaInformation(grammar.getName(),
                            "Masks random word of sentence.",
                            "SingleMaskingTask",
                            Randomizer.getInstance().getSeed());
            return new DataSet(metaInformation, datumIterator);
        });
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
