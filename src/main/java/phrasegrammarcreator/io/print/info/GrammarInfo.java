package phrasegrammarcreator.io.print.info;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.print.Util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class GrammarInfo extends InfoWatcher<FormalGrammar>{
    

    public GrammarInfo(PrintStream stream, FormalGrammar grammar) {
        super(stream, grammar);
    }

    @Override
    public String toString(FormalGrammar watched) {
        PrintStream previous = out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();
        try (PrintStream ps = new PrintStream(baos, true, utf8)) {
            out = ps;
            printName();
            printVocabulary();
            printRules();
            printDictionary();
            printPossibleDerivations();
            printPossibleDerivations();
            String printed = baos.toString(utf8);
            out = previous;
            baos.close();
            return printed;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void printName() {
        out.println("Name: " + watched.getName());
        out.println();
    }
    public void printRules() {
        String header = "Rules:";
        out.println(Util.printCollection(header, watched.getRules(), Rule::toString));
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

    public void printPossibleDerivations() {
        String header = "Possible Derivations:";
        out.println(Util.printCollection(
                header,
                watched.getNextPossibleDerivations(),
                d -> d.getRule().toString(),
                true));
        out.println();
    }
}
