package phrasegrammarcreator.io.console.info;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.console.Util;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class GrammarInfo extends InfoWatcher<FormalGrammar>{
    

    public GrammarInfo(PrintStream stream, FormalGrammar grammar) {
        super(stream, grammar);
    }

    @Override
    public String toString(FormalGrammar watched) {
        return printToString(
            this::printName,
            this::printVocabulary,
            this::printRules,
            this::printDictionary,
            this::printStartPhrase
        );
    }

    public void printName() {
        out.println("Name: " + watched.getName());
        out.println();
    }
    public void printRules() {
        String header = "Rules:";
        out.println(Util.formatCollection(header, watched.getRules(), Rule::toString));
        out.println();
    }

    public void printVocabulary() {
        StringBuilder builder = new StringBuilder("Vocabulary:");
        Collection<Variable> c = watched.getVocabulary().getVariables();
        List<String> terminals = c.stream()
                .filter(v -> v instanceof Terminal)
                .map(Variable::toString)
                .toList();
        builder.append("\n\t Terminals: \t" + terminals);

        List<String> non_terminals = c.stream()
                .filter(v -> v instanceof NonTerminal)
                .map(Variable::toString)
                .toList();
        builder.append("\n\t Non-Terminals: \t" + non_terminals);
        builder.append("\n");
        out.println(builder.toString());
    }

    public void printDictionary() {
        StringBuilder builder = new StringBuilder("Dictionary:\n");
        WordDictionary dictionary = watched.getDictionary();
        builder.append(dictionary.toString());
        builder.append("\n");
        out.println(builder);
    }

    public void printStartPhrase() {
        out.println("Start phrase: ");
        out.println("\t" + watched.getStartPhrase());
        out.println();
    }
}
