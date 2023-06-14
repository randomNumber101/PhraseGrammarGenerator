package phrasegrammarcreator.core;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.*;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CNFGrammar extends FormalGrammar {


    CfRuleContainer originalRules;
    List<ContextFreeRule> computedRules;
    HashMap<Terminal, NonTerminal> pseudoTerminals = new HashMap<>();

    public CNFGrammar(String name, Vocabulary vocabulary, List<Rule> rules, WordDictionary dictionary, Phrase startPhrase) {
        this(new FormalGrammar(name, vocabulary, rules, dictionary, startPhrase));
    }

    public CNFGrammar(FormalGrammar grammar) {
        super();
        transformOf(grammar);
    }

    public void transformOf(FormalGrammar grammar) {
        this.vocabulary = new Vocabulary(grammar.getVocabulary());
        this.name = grammar.getName() + "_CNF";
        this.dictionary = grammar.dictionary;

        computedRules = new ArrayList<>();

        // Create a new list of CF Rules & Force them to be CF
        this.originalRules = new CfRuleContainer(grammar.rules);


        isolateTerminals();
        binarizeRightSide();
        // killUnitDerivations() // Could be added for completions' sake, but unit derivations don't occur practically

        this.ruleContainer = new CfRuleContainer((List<Rule>) (List<?>) computedRules);
        this.rules = (List<Rule>) (List<?>) ruleContainer.getRules();
    }

    public NonTerminal getPseudoTerminal(Terminal terminal) {
        if (pseudoTerminals.containsKey(terminal))
            return pseudoTerminals.get(terminal);

        NonTerminal newEntry = (NonTerminal) Variable.ofRegex(Variable.Type.NON_TERMINAL, vocabulary,terminal.getRegex() + "_p", null);
        pseudoTerminals.put(terminal, newEntry);

        // Add rule: pseudoTerminal -> terminal
        ContextFreeRule pseudoTerminalRule = new ContextFreeRule(terminal.getRegex() + "_p", newEntry, terminal.toPhrase());
        computedRules.add(pseudoTerminalRule);

        return newEntry;
    }


    private void isolateTerminals() {
        for (ContextFreeRule r : originalRules.getRules()) {
            NonTerminal lhs = r.getLHS();
            Phrase rhs = r.getRHS();
            Phrase newRhs = new Phrase();

            for (VariableInstance vi : rhs) {
                switch (vi.getBuilder().getType()) {
                    case NON_TERMINAL, WORD -> newRhs.add(vi);
                    case TERMINAL -> {
                        NonTerminal pseudo = getPseudoTerminal((Terminal) vi.getBuilder());
                        newRhs.add(pseudo.createInstance());
                    }
                    default -> {
                    }
                }
            }
            computedRules.add(new ContextFreeRule(r.getName(), lhs, newRhs));
        }
    }

    private NonTerminal createRuleConnector(String ruleName, NonTerminal lhs, NonTerminal rhs) {
        NonTerminal connector = (NonTerminal) Variable.ofRegex(Variable.Type.NON_TERMINAL, vocabulary, lhs.toString() + ruleName, "CNF_connect_" + ruleName);
        Phrase newRhs = new Phrase(List.of(lhs, connector));
        ContextFreeRule newRule = new ContextFreeRule(ruleName, lhs, newRhs);
        computedRules.add(newRule);
        return connector;
    }

    private void binarizeRightSide() {
        List<ContextFreeRule> removeAfterwards = new ArrayList<>();
        for (ContextFreeRule r : computedRules) {
            NonTerminal lhs = r.getLHS();
            Phrase rhs = r.getRHS();
            if (rhs.size() > 2) {
                // Remove old rule (after iteration)
                removeAfterwards.add(r);

                // Connect RHS to first element of LHS
                NonTerminal connector = createRuleConnector(r.getName(), lhs, (NonTerminal) rhs.get(0).getBuilder());

                // Connect in-between variables on RHS
                for (int i = 0; i < rhs.size() - 2; i++) {
                    if (rhs.get(i).getBuilder() instanceof NonTerminal nt) {
                        connector = createRuleConnector(r.getName() + "_c" + i, connector, nt);
                    }
                }

                // Last rule derives to the two last remaining variables
                Variable last = rhs.get(rhs.size() - 1).getBuilder();
                Variable sndLast = rhs.get(rhs.size() - 2).getBuilder();
                Phrase lastTwo = new Phrase(List.of(last, sndLast));
                ContextFreeRule lastTwoRule = new ContextFreeRule(r.getName() + "_c" + (rhs.size() - 2), connector, lastTwo);
                computedRules.add(lastTwoRule);
            }
        }
        computedRules.removeAll(removeAfterwards);
    }
}
