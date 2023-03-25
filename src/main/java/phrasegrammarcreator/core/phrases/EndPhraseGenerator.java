package phrasegrammarcreator.core.phrases;

import org.jetbrains.annotations.NotNull;
import phrasegrammarcreator.compute.calculate.DerivationsCalculator;
import phrasegrammarcreator.compute.pick.derivation.DerivationChooser;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.impl.DerivationTree;

import java.util.function.Function;

public class EndPhraseGenerator implements Function<Phrase, EndPhrase> {

    private DerivationsCalculator calculator;
    private DerivationChooser chooser;

    private FormalGrammar grammar;

    public EndPhraseGenerator(FormalGrammar grammar, DerivationsCalculator calculator, DerivationChooser chooser) {
        this.grammar = grammar;
        this.calculator = calculator;
        this.chooser = chooser;

    }

    @Override
    public EndPhrase apply(@NotNull Phrase phrase) {
        DerivationTree path = new DerivationTree(phrase);
        DerivationNode next = path.deriveHead(calculator, chooser);
        while (next != null) {
            next = path.deriveHead(calculator, chooser);
        }
        return EndPhrase.ofPhrase(grammar, path.getHead());
    }
}
