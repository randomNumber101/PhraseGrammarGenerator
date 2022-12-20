package phrasegrammarcreator.io.print;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;

import java.util.Collection;
import java.util.List;

public class GrammarInfo {

    private FormalGrammar grammar;

    public GrammarInfo(FormalGrammar grammar) {
        this.grammar = grammar;
    }
    public String printName() {
        return "Name: " + grammar.getName();
    }
    public String printRules() {
        String header = "Rules:";
        return Util.printCollection(header, grammar.getRules(), Rule::toString);
    }

    public String printVocabulary() {
        StringBuilder builder = new StringBuilder("Vocabulary:");
        Collection<Variable> c = grammar.getVocabulary().getVariables();
        List<String> terminals = c.stream()
                .filter(v -> v instanceof Terminal)
                .map(Variable::toString)
                .toList();
        builder.append("\n\t Terminals:" + terminals);

        List<String> non_terminals = c.stream()
                .filter(v -> v instanceof NonTerminal)
                .map(Variable::toString)
                .toList();
        builder.append("\n\t Non-Terminals:" + non_terminals);

        return builder.toString();
    }

    public String printDictionary() {
        StringBuilder builder = new StringBuilder("Dictionary:\n");
        WordDictionary dictionary = grammar.getDictionary();
        builder.append(dictionary.toString());
        return builder.toString();

    }
}
