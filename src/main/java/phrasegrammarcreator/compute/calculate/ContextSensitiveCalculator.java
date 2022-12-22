package phrasegrammarcreator.compute.calculate;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.DerivationSet;
import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.compute.calculate.match.SuffixTree;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.rules.ContextSensitiveRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.HashMap;
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
        Tokenizer tokenizer = new Tokenizer(p);
        SuffixTree suffixTree = new SuffixTree(tokenizer.getTokenizedPhrase());
        DerivationSet output = new DerivationSet();
        for (Rule r : rules) {
            List<Occurence> occurrences = suffixTree.searchText(tokenizer.tokenize(r.getSource().toPhrase()));
            output.add(r, occurrences);
        }
        return output;
    }


    static class Tokenizer {

        public final static int CHAR_RANGE = 65535;
        char NONE = (char) (CHAR_RANGE - 1);
        final private Phrase phrase;
        final private String tokenizedPhrase;

        private final char start = 'A';
        private int index = 0;

        private
        final HashMap<Variable, Character> tokenMap = new HashMap<>();

        Tokenizer(Phrase phrase) {
            this.phrase = phrase;
            StringBuilder tokenizedPhraseBuilder = new StringBuilder();
            for (VariableInstance<?> instance : phrase) {
                tokenizedPhraseBuilder.append(createToken(instance.getBuilder()));
            }
            tokenizedPhrase = tokenizedPhraseBuilder.toString();
        }

        public String tokenize(Phrase phrase) {
            StringBuilder tokenizedPhraseBuilder = new StringBuilder();
            for (VariableInstance<?> instance : phrase) {
                tokenizedPhraseBuilder.append(getToken(instance.getBuilder()));
            }
            return tokenizedPhraseBuilder.toString();
        }

        private Character createToken(Variable variable) {
            if (tokenMap.containsKey(variable))
                return tokenMap.get(variable);
            char newCharToken = (char) ((start + index) % (CHAR_RANGE - 1));
            index++;
            tokenMap.put(variable, newCharToken);
            return newCharToken;
        }

        private Character getToken(Variable variable) {
            if (tokenMap.containsKey(variable))
                return tokenMap.get(variable);
            return NONE;
        }

        public String getTokenizedPhrase() {
            return tokenizedPhrase;
        }

        public Phrase getPhrase() {
            return phrase;
        }
    }


}
