package phrasegrammarcreator.core.phrases;

import phrasegrammarcreator.compute.pick.words.WordGenerator;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.phrases.variables.words.WordDictionary;
import phrasegrammarcreator.core.phrases.variables.words.WordTerminal;

import java.util.ArrayList;

public class EndPhrase extends ArrayList<Variable> {



    public enum BuildingPolicy {
        WORDS_ONLY,
        VARIABLE_NAMES;
    }

    private EndPhrase() {}

    // TODO: Generators cannot operate context sensitive yet
    public String generateSentence(WordGenerator<Variable> generator) {
        return this.stream()
                .map(generator::nextString)
                .map(s -> s + " ")
                .reduce("", String::concat)
                .strip();
    }


    public static EndPhrase ofPhrase(BuildingPolicy policy, FormalGrammar grammar, DerivationNode node) {
        EndPhrase endPhrase = new EndPhrase();
        if (validate(policy, grammar, node)) {
            WordDictionary dictionary = grammar.getDictionary();
            for (VariableInstance<?> instance : node.getData()) {
                if (instance.getBuilder() instanceof Terminal) {
                    WordTerminal wordTerminal = dictionary.toWord((Terminal) instance.getBuilder());
                    endPhrase.add(wordTerminal);
                }
                else {
                    switch (policy) {
                        case WORDS_ONLY -> throw new IllegalArgumentException("Only terminals allowed in EndPhrases.");
                        case VARIABLE_NAMES -> {
                            endPhrase.add(instance.getBuilder());
                        }
                    }
                }
            }

        }
        return endPhrase;
    }

    public static boolean validate(BuildingPolicy policy, FormalGrammar grammar, DerivationNode node) {
        WordDictionary wordDictionary = grammar.getDictionary();
        if (!grammar.getPossibleDerivations(node).isEmpty())
            return false;
        for (VariableInstance<?> i : node.getData()) {
            switch (policy) {
                case WORDS_ONLY -> {
                    if (!(i.getBuilder() instanceof Terminal)) return false;
                }
                case VARIABLE_NAMES -> {
                    continue;
                }
            }
        }
        return true;
    }

}
