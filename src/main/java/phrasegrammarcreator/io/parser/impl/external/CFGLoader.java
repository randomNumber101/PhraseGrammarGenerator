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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static phrasegrammarcreator.core.phrases.variables.Variable.Type.NON_TERMINAL;
import static phrasegrammarcreator.core.phrases.variables.Variable.Type.TERMINAL;

public class CFGLoader {

    final String ruleRegex = "([\\w']*) -> ([\\w']+( [\\w']+)*)(\\s*\\|\\s*[\\w']+( [\\w']+)*)*";
    final String dictRegex = "([\\w']*) -> ((\"[\\w'.,]+\")(\\s*\\|\\s*(\"[\\w'.,]+\"))*)";
    final String startSymbolRegex = "%start ([\\w']+)";

    private File file;
    private String name;

    public CFGLoader(File file, String name) {
        this.file = file;
        this.name = name;
    }

    public FormalGrammar load() throws Exception {

        Pattern rulePattern = Pattern.compile(ruleRegex, Pattern.CASE_INSENSITIVE);
        Pattern dictPattern = Pattern.compile(dictRegex, Pattern.CASE_INSENSITIVE);
        Pattern startSymbolPattern = Pattern.compile(startSymbolRegex, Pattern.CASE_INSENSITIVE);
        
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
            String[] rhs = matcher.group(2).strip().split("[ |]");

            synchronized (CFGLoader.class) {
                allVariables.addAll(List.of(rhs));
                nonTerminals.add(lhs);
            }
        });

        // Register nonTerminals
        Vocabulary vocabulary = new Vocabulary();
        nonTerminals.forEach(regex -> Variable.ofRegex(NON_TERMINAL, vocabulary, regex, null));

        //Register terminals and save them for later
        List<Terminal> terminals = new ArrayList<>();
        allVariables.removeAll(nonTerminals); // all - non terminals = terminals
        allVariables.forEach(regex -> terminals.add((Terminal) Variable.ofRegex(TERMINAL, vocabulary, regex, null)));
        vocabulary.setMaskWorthy(terminals); // Make all terminals mask worthy, TODO: This is not configurarble yet

        // Create dictionary
        WordDictionary wordDictionary = new WordDictionary(terminals);

        // Parse rules now ( .cfg rule format is compatible with this custom format)
        RuleParser parser = new RuleParser(vocabulary);
        List<Rule> rules = new ArrayList<>();
        reader =  new BufferedReader(new FileReader(file));
        reader.lines().parallel().forEach(line -> {
            if (!rulePattern.matcher(line).find()) {
                Matcher dictMatcher = dictPattern.matcher(line);
                if (dictMatcher.find()) {
                    // Add all rules of form (A -> "a1" | "a2" | ...) to dictionary instead of creating rules
                    String lhs = dictMatcher.group(1);
                    String rhs = dictMatcher.group(2);

                    Terminal terminal = (Terminal) Variable.ofRegex(TERMINAL, vocabulary, lhs, null);
                    List<String> entries = Arrays.stream(rhs.split("\\|"))
                            .map(String::strip)
                            .map(s -> s.replaceAll("\"", "")).toList();
                    wordDictionary.add(terminal, entries);
                }
                return;
            }
            try {
                synchronized (CFGLoader.class) {
                    rules.addAll(parser.parse(line));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Parse start phrase
        PhraseParser phraseParser = new PhraseParser(vocabulary);
        Phrase startPhrase = phraseParser.parse(startSymbol.get());
        
        return new FormalGrammar(name, vocabulary, rules, wordDictionary, startPhrase);
    }
}
