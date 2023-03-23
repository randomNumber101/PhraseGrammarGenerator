package phrasegrammarcreator.core.phrases;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.phrases.words.WordTerminal;

import java.util.ArrayList;

public class EndPhrase extends ArrayList<WordTerminal> {

    FormalGrammar grammar;
    DerivationNode node;

    private EndPhrase(FormalGrammar grammar, DerivationNode node) {
        this.grammar = grammar;
        this.node = node;
    }

    public static EndPhrase ofPhrase(FormalGrammar grammar, DerivationNode node) {
        EndPhrase endPhrase = new EndPhrase(grammar, node);
        if (validate(grammar, node)) {
            WordDictionary dictionary = grammar.getDictionary();
            for (VariableInstance<?> instance : node.getData()) {
                if (instance.getBuilder() instanceof Terminal terminal) {
                    WordTerminal wordTerminal = dictionary.toWord(terminal);
                    endPhrase.add(wordTerminal);
                }
                else {
                    throw new IllegalArgumentException("Only terminals allowed in EndPhrases.");
                }
            }

        }
        return endPhrase;
    }

    public static boolean validate(FormalGrammar grammar, DerivationNode node) {
        if (!grammar.getPossibleDerivations(node).isEmpty())
            return false;
        for (VariableInstance<?> i : node.getData()) {
            if (!(i.getBuilder() instanceof Terminal)) return false;
        }
        return true;
    }

    public DerivationNode getNode() {
        return node;
    }
}
