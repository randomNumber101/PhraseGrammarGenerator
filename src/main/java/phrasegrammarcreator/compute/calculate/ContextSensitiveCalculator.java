package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.calculate.match.SuffixTree;
import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.rules.ContextSensitiveRule;
import phrasegrammarcreator.core.rules.Rule;
import java.util.List;

public class ContextSensitiveCalculator extends DerivationsCalculator{
    protected ContextSensitiveCalculator(List<ContextSensitiveRule> rules) {
        super((List<Rule>) (List<?>)rules);

    }

    @Override
    public DerivationSet calculate(Phrase p, DerivationSet lastPossibleDerivations, Derivation lastPicked) {
        return calculateNaive(p);
    }

    private  DerivationSet calculateNaive(Phrase p) {
        String separator = " ";
        String phraseString = p.toString(separator);

        // TODO: Create Suffix Tree out of former tree
        SuffixTree suffixTree = new SuffixTree(phraseString);
        DerivationSet output = new DerivationSet();
        for (Rule r : rules) {
            List<Occurence> occurrences = suffixTree.searchText(r.getSource().toPhrase().toString(separator));
            output.add(r, occurrences);
        }
        return output;
    }
}
