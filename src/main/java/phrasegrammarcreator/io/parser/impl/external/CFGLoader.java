package phrasegrammarcreator.io.parser.impl.external;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.Variable.Type.*;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.parser.impl.PhraseParser;
import phrasegrammarcreator.io.parser.impl.RuleParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static phrasegrammarcreator.core.phrases.variables.Variable.Type.NON_TERMINAL;
import static phrasegrammarcreator.core.phrases.variables.Variable.Type.TERMINAL;

public class CFGLoader {

    final String ruleRegex = "(\\w*) -> (\\w+( \\w+)*)";
    final String startSymbolRegex = "%start (\\w+)";

    private File file;
    private String name;

    public CFGLoader(File file, String name) {
        this.file = file;
    }

    public FormalGrammar load() throws Exception {

        Pattern rulePattern = Pattern.compile(ruleRegex);
        Pattern startSymbolPattern = Pattern.compile(startSymbolRegex);
        
        AtomicReference<String> startSymbol = new AtomicReference<>();
        HashSet<String> allVariables = new HashSet<>();
        HashSet<String> nonTerminals = new HashSet<>();

        // Load all used variables
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.lines().parallel().forEach(line -> {
            Matcher matcher = rulePattern.matcher(line);
            if (!matcher.find()) {
                matcher = startSymbolPattern.matcher(line);
                if (matcher.find())
                    startSymbol.set(matcher.group(1));
                return;
            }
                
            String lhs = matcher.group(1);
            String[] rhs = matcher.group(2).split(" ");

            allVariables.addAll(List.of(rhs));
            nonTerminals.add(lhs);
        });

        // Register nonTerminals
        Vocabulary vocabulary = new Vocabulary();
        nonTerminals.forEach(regex -> Variable.ofRegex(NON_TERMINAL, vocabulary, regex, null));

        //Register terminals and save them for later
        List<Terminal> terminals = new ArrayList<>();
        allVariables.removeAll(nonTerminals); // all - non terminals = terminals
        allVariables.forEach(regex -> terminals.add((Terminal) Variable.ofRegex(TERMINAL, vocabulary, regex, null)));

        // Parse rules now ( .cfg rule format is compatible with this custom format)
        RuleParser parser = new RuleParser(vocabulary);
        List<Rule> rules = new ArrayList<>();
        reader =  new BufferedReader(new FileReader(file));
        reader.lines().parallel().forEach(line -> {
            if (!rulePattern.matcher(line).find())
                return;
            try {
                rules.addAll(parser.parse(line));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Empty Dict as Grammar doesn't necessarily have dict format
        WordDictionary wordDictionary = new WordDictionary(terminals);

        // Parse start phrase
        PhraseParser phraseParser = new PhraseParser(vocabulary);
        Phrase startPhrase = phraseParser.parse(startSymbol.get());

        return new FormalGrammar(name, vocabulary, rules, wordDictionary, startPhrase);
    }
}
