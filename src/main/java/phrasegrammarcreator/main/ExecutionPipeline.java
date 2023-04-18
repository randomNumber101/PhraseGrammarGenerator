package phrasegrammarcreator.main;

import phrasegrammarcreator.compute.calculate.ContextFreeCalculator;
import phrasegrammarcreator.compute.pick.derivation.SmartChooser;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.PossibilitiesGenerator;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.EndPhraseGenerator;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.words.generate.BracketTreeGenerator;
import phrasegrammarcreator.core.phrases.words.generate.OutputGenerator;
import phrasegrammarcreator.core.phrases.words.generate.RandomMaskingGenerator;
import phrasegrammarcreator.io.out.FileGenerator;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.io.out.jsonObjects.MetaInformation;
import phrasegrammarcreator.util.Randomizer;
import phrasegrammarcreator.util.futures.ListFuture;
import phrasegrammarcreator.util.futures.SimpleFuture;
import phrasegrammarcreator.util.pipeline.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExecutionPipeline extends AbstractPipe<GenerationInstance, DataSet> {


    Randomizer randomizer;

    ForkPipe<GenerationInstance, Phrase> possibilityPipe;

    ParallelPipe<Phrase, EndPhrase> derivationPipe;

    ParallelPipe<EndPhrase, Datum> dataPipe;

    JoinPipe<Datum, DataSet> dataMergePipe;

    InterceptorPipe<DataSet> dataSetSaver;

    private HashMap<String, SimpleFuture<?>> stats = new HashMap<>();


    @Override
    public DataSet apply(GenerationInstance generationInstance) {
        build(generationInstance);

        return possibilityPipe
                .andThen(derivationPipe)
                .andThen(dataPipe)
                .andThen(dataMergePipe)
                .andThen(dataSetSaver)
                .apply(generationInstance);
    }

    public void build(GenerationInstance instance) {
        FormalGrammar grammar = instance.grammar();
        Settings settings = instance.settings();

        randomizer = new Randomizer();
        if (settings.seed() != null)
            randomizer.setSeed(settings.seed());

        possibilityPipe = buildPossibilityPipe(grammar, settings);
        derivationPipe = buildDerivationPipe(grammar, settings);
        dataPipe = buildDataPipe(grammar, settings);
        dataMergePipe = buildMergePipe(grammar, settings);
        dataSetSaver = buildDataSaver(grammar, settings);
    }


    private ForkPipe<GenerationInstance, Phrase> buildPossibilityPipe(FormalGrammar grammar, Settings settings) {
        int possibilityCap = settings.possibilityCap();
        int depthCap = settings.depthCap();
        PossibilitiesGenerator pg = new PossibilitiesGenerator(grammar, grammar.getStartPhrase(), possibilityCap, depthCap);

        stats.put("phrase count", pg::getCurrentCount);
        stats.put("search tree depth", pg::getCurrentDepth);
        return new ForkPipe<>(pg);
    }

    private ParallelPipe<Phrase, EndPhrase> buildDerivationPipe(FormalGrammar grammar, Settings settings) {
        ContextFreeCalculator calculator = new ContextFreeCalculator(grammar.getRuleContainer());
        SmartChooser chooser = new SmartChooser(grammar, randomizer);
        EndPhraseGenerator endPhraseGenerator = new EndPhraseGenerator(grammar, calculator, chooser);
        return new ParallelPipe<>(endPhraseGenerator);
    }

    private ParallelPipe<EndPhrase, Datum> buildDataPipe(FormalGrammar grammar, Settings settings) {
        OutputGenerator datumGenerator = switch (settings.task()) {
            case MASKING -> new RandomMaskingGenerator(randomizer, settings.policy());
            case TREE_BRACKETING -> new BracketTreeGenerator(randomizer, settings.policy());
            default -> null;
        };

        Function<List<Integer>, Integer> sumFunction = l -> l.stream().mapToInt(Integer::intValue).sum();
        ListFuture<Integer, Integer> datumCountsFeature = new ListFuture<>(sumFunction);

        stats.put("data count", datumCountsFeature);

        Function<List<Datum>, List<Datum>> statInterceptor = data ->
                {
                    datumCountsFeature.add(data::size);
                    return data;
                } ;

        return new ParallelPipe<>(new ForkPipe<>(datumGenerator.andThen(statInterceptor).andThen(List::iterator)));
    }

    private JoinPipe<Datum, DataSet> buildMergePipe(FormalGrammar grammar, Settings settings) {

        // TODO add description parameter to settings
        SimpleFuture<MetaInformation> metaInformationFuture = () -> {
            String description = grammar.getName() + " " + settings.task();

            Map<String, Object> evaluatedStats = stats.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            f -> f.getValue().get()
                    ));

            MetaInformation metaInformation =
                    new MetaInformation(
                            grammar.getName(),
                            description,
                            settings.task().toString(),
                            randomizer.getSeed(),
                            evaluatedStats
                    );
            return metaInformation;
        };

        return new JoinPipe<>(datumIterator -> new DataSet(metaInformationFuture, datumIterator));
    }

    private InterceptorPipe<DataSet> buildDataSaver(FormalGrammar grammar, Settings settings) {
        return new InterceptorPipe<>(dataSet -> FileGenerator.save(settings.outputDir(), dataSet));
    }
}
