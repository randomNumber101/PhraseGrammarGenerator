package phrasegrammarcreator.core.parser;

import phrasegrammarcreator.core.CNFGrammar;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.util.IteratorTools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

// TODO: Implement Parser for CF Grammars
public class CYKParser {

    private CNFGrammar grammar;

    public CYKParser(FormalGrammar grammar) {
        if (grammar instanceof CNFGrammar cnf) {
            this.grammar = cnf;
        }
        else {
            this.grammar = new CNFGrammar(grammar);
        }
    }


    public boolean parse(String sentence) throws Exception {
        String[] parts = sentence.strip().split("\\s+");
        int N = parts.length;
        HashSet<NonTerminal>[][] T = new HashSet[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                T[i][j] = new HashSet<>();
            }
        }

        for (int i = 0; i < N; i++) {
            List<Terminal> possible = grammar.getDictionary().getPossibleTerminals(parts[i]);
            for (Terminal terminal : possible) {
                List<ContextFreeRule> unaryRules = grammar.getRuleContainer().getRulesWithRHS(new Phrase(List.of(terminal)));
                T[i][0].addAll(unaryRules.stream().map(ContextFreeRule::getLHS).toList());
            }
        }

        for (int span = 2; span <= N; span++) {
            for (int i = 0; i < N - span + 1; i++) {
                T[i][span - 1] = new HashSet<>();
                for (int k = 1; k < span; k++) {
                    HashSet<NonTerminal> lPartition = T[i][k - 1];
                    HashSet<NonTerminal> rPartition = T[i + k][span - k - 1];
                    // Iterate over all combinations, If one partition is empty no combinations will be provided
                    Iterator<List<NonTerminal>> combinations = IteratorTools.combine(List.of(lPartition.iterator(), rPartition.iterator()));
                    for (Iterator<List<NonTerminal>> it = combinations; it.hasNext(); ) {
                        List<Variable> combination = (List<Variable>) (List<?>) it.next();
                        // Find rules where RHS equals the combination (e.g. A B) and add their source to
                        List<ContextFreeRule> possibleRules = grammar.getRuleContainer().getRulesWithRHS(new Phrase(combination));
                        T[i][span - 1].addAll(possibleRules.stream().map(ContextFreeRule::getLHS).toList());
                    }
                }
            }
        }

        Phrase startPhrase = grammar.getStartPhrase();
        if (startPhrase.size() > 1)
            throw new IllegalArgumentException("CYK-Parsing is currently only supported for grammars with a single start symbol!");
        else {
            Variable startSymbol = startPhrase.get(0).getBuilder();
            if (startSymbol instanceof NonTerminal startSymbolNT) {
                return T[0][N - 1].contains(startSymbolNT);
            }
            else
                throw new IllegalArgumentException("Start symbol has to be a Non-Terminal!");
        }
    }
}
