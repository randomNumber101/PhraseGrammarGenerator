package phrasegrammarcreator.main;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.calculate.ContextFreeCalculator;
import phrasegrammarcreator.compute.pick.derivation.SmartChooser;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.PossibilitiesGenerator;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.EndPhraseGenerator;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.io.out.generate.*;
import phrasegrammarcreator.io.out.FileGenerator;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.io.out.jsonObjects.MetaInformation;
import phrasegrammarcreator.util.IteratorTools;
import phrasegrammarcreator.util.Randomizer;
import phrasegrammarcreator.util.futures.ListFuture;
import phrasegrammarcreator.util.futures.SimpleFuture;
import phrasegrammarcreator.util.pipeline.*;

import java.util.HashMap;
import java.util.Iterator;
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

    private final HashMap<String, SimpleFuture<?>> stats = new HashMap<>();


    @Override
    public DataSet apply(GenerationInstance generationInstance) {
        build(generationInstance);

        CollectorPipe<Phrase> collector = new CollectorPipe();
        ParallelPipe<EndPhrase, EndPhrase> lengthFilter = new ParallelPipe<>(new ForkPipe<>(new Function<EndPhrase, Iterator<EndPhrase>>() {
            @Override
            public Iterator<EndPhrase> apply(EndPhrase endPhrase) {
                if (endPhrase.size() > generationInstance.settings().lengthCap())
                    return IteratorTools.getEmpty();
                else
                    return List.of(endPhrase).iterator();
            }
        }));


        return possibilityPipe
                .andThen(collector)
                .andThen(derivationPipe)
                .andThen(lengthFilter)
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
        dataSetSaver = buildDataSaver(instance);
    }


    private ForkPipe<GenerationInstance, Phrase> buildPossibilityPipe(FormalGrammar grammar, Settings settings) {
        int possibilityCap = settings.possibilityCap();
        int depthCap = settings.depthCap();
        PossibilitiesGenerator pg = new PossibilitiesGenerator(grammar, grammar.getStartPhrase(), randomizer, possibilityCap, depthCap);

        stats.put("phrase count", pg::getCurrentCount);
        stats.put("search tree depth", pg::getCurrentDepth);
        return new ForkPipe<>(pg);
    }

    private ParallelPipe<Phrase, EndPhrase> buildDerivationPipe(FormalGrammar grammar, Settings settings) {
        ContextFreeCalculator calculator = new ContextFreeCalculator(grammar.getRuleContainer());
        SmartChooser chooser = new SmartChooser(grammar, randomizer, SmartChooser.STRATEGY.NARROW_DOWN_LINEAR);
        EndPhraseGenerator endPhraseGenerator = new EndPhraseGenerator(grammar, calculator, chooser);
        return new ParallelPipe<>(endPhraseGenerator);
    }

    private ParallelPipe<EndPhrase, Datum> buildDataPipe(FormalGrammar grammar, Settings settings) {
        OutputGenerator datumGenerator = switch (settings.task()) {
            case MASKING -> new SelectiveMaskingGenerator(randomizer, settings.policy());
            case CLASS_MASKING -> new SelectiveMaskingClassGenerator(randomizer, settings.policy());
            case TREE_BRACKETING -> new BracketTreeGenerator(randomizer, settings.policy());
            case BINARY_CLASSIFICATION -> new ClassificationGenerator(randomizer, settings.policy(), grammar.getRuleContainer(), grammar);
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

            return new MetaInformation(
                grammar.getName(),
                description,
                settings.task().toString(),
                randomizer.getSeed(),
                evaluatedStats
            );
        };

        return new JoinPipe<>(datumIterator -> new DataSet(metaInformationFuture, datumIterator));
    }

    private InterceptorPipe<DataSet> buildDataSaver(GenerationInstance generation) {
        return new InterceptorPipe<>(dataSet -> FileGenerator.save(generation, dataSet));
    }
}
