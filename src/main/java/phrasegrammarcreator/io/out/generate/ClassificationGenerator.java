package phrasegrammarcreator.io.out.generate;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// ToDo: What task to implement?
public class ClassificationGenerator extends BracketTreeGenerator{

    CfRuleContainer container;
    public ClassificationGenerator(Random random, WordGenerationPolicy policy, CfRuleContainer container) {
        super(random, policy);
        this.container = container;
    }

    @Override
    public void initialize(EndPhrase ep) {
        super.initialize(ep);

    }

    protected Function<List<String>, List<Datum>> getDatumGenerator(Function<List<String>, String> input, Function<List<String>, String> label) {
        return parts -> {
            return new ArrayList<>();
        };
    }

}
