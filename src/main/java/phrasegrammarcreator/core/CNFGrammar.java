package phrasegrammarcreator.core;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.*;
import phrasegrammarcreator.core.phrases.words.WordDictionary;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.util.ListModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CNFGrammar extends FormalGrammar {


    List<ContextFreeRule> originalRules;
    ListModifier<ContextFreeRule> changes;
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
        this.startPhrase = grammar.startPhrase;

        // Create a new list of CF Rules & Force them to be CF
        this.originalRules = new CfRuleContainer(grammar.rules).getRules();
        changes = new ListModifier<>();

        // TERM
        isolateTerminals();
        changes.apply(originalRules);

        // BIN
        binarizeRightSide();
        changes.apply(originalRules);

        // UNIT
        do {
            changes.apply(originalRules);
            killUnitRulesIteration();
        }
        while (!changes.isEmpty());

        this.rules = (List<Rule>) (List<?>) originalRules;
        this.ruleContainer = new CfRuleContainer(rules);
    }

    /**
     * Returns (and creates) a unique Pseudo-Terminal (Non-Terminal)
     * Creates a rule connecting the Pseudo-Terminal to the Terminal
     * @param terminal the terminal
     * @return  a unique pseudo terminal dedicated to the specified terminal
     */
    public NonTerminal getPseudoTerminal(Terminal terminal) {
        if (pseudoTerminals.containsKey(terminal))
            return pseudoTerminals.get(terminal);

        NonTerminal newEntry = (NonTerminal) Variable.ofRegex(Variable.Type.NON_TERMINAL, vocabulary,terminal.getRegex() + "_p", null);
        pseudoTerminals.put(terminal, newEntry);

        // Add rule: pseudoTerminal -> terminal
        ContextFreeRule pseudoTerminalRule = new ContextFreeRule(terminal.getRegex() + "_p", newEntry, terminal.toPhrase());
        changes.addition(pseudoTerminalRule);

        return newEntry;
    }


    /**
     * Terminals on a RHS will be replaced by pseudo-terminals so that each production with terminals will be of form:
     *  PSEUDO_T -> T, where PSEUDO_T is a unique Non-Terminal pointing to the Terminal
     */
    private void isolateTerminals() {

        for (ContextFreeRule r : originalRules) {
            NonTerminal lhs = r.getLHS();
            Phrase rhs = r.getRHS();
            Phrase newRhs = new Phrase();

            boolean changed = false;
            for (VariableInstance vi : rhs) {
                switch (vi.getBuilder().getType()) {
                    case NON_TERMINAL, WORD -> newRhs.add(vi);  // NON_TERMINALS, WORDS stay the same
                    case TERMINAL -> {
                        changed = true;
                        // Terminals will be replaced by a unique NonTerminal (Pseudo Terminals)
                        NonTerminal pseudo = getPseudoTerminal((Terminal) vi.getBuilder());
                        newRhs.add(pseudo.createInstance());
                    }
                    default -> {}
                }
            }
            // Only do something if there was a terminal on RHS
            if (changed) {
                changes.addition(new ContextFreeRule(r.getName(), lhs, newRhs));
                changes.deletion(r);
            }
        }
    }

    /**
     * Creates a new rule connection lhs to rhs + a connection non-terminal that will be returned for further chaining
     * i.e.
     *  rhs -> lhs connector
     * @param ruleName  the name of the newly created rule
     * @param lhs left-hand side non-terminal
     * @param rhs right-hand side non-terminal to connect with lhs
     * @return the connector non-terminal; returned for further chaining
     */
    private NonTerminal createRuleConnector(String ruleName, NonTerminal lhs, NonTerminal rhs) {
        NonTerminal connector = (NonTerminal) Variable.ofRegex(Variable.Type.NON_TERMINAL, vocabulary, lhs.toString() + ruleName, "CONNECT_" + ruleName);
        Phrase newRhs = new Phrase(List.of(rhs, connector));
        ContextFreeRule newRule = new ContextFreeRule(ruleName, lhs, newRhs);
        changes.addition(newRule);
        return connector;
    }

    /**
     * Splits every production with rhs.size() > 2 into little productions with rhs.size() == 2
     * Creates new unique connection non-terminals and rules if needed
     */
    private void binarizeRightSide() {
        for (ContextFreeRule r : originalRules) {
            NonTerminal lhs = r.getLHS();
            Phrase rhs = r.getRHS();
            if (rhs.size() > 2) {
                // Remove old rule (after iteration)
                changes.deletion(r);

                // Connect RHS to first element of LHS
                NonTerminal connector = createRuleConnector(r.getName(), lhs, (NonTerminal) rhs.get(0).getBuilder());

                // Connect in-between variables on RHS
                for (int i = 1; i < rhs.size() - 2; i++) {
                    if (rhs.get(i).getBuilder() instanceof NonTerminal nt) {
                        connector = createRuleConnector(r.getName() + "_c" + i, connector, nt);
                    }
                }

                // Last rule derives to the two last remaining variables
                Variable sndLast = rhs.get(rhs.size() - 2).getBuilder();
                Variable last = rhs.get(rhs.size() - 1).getBuilder();
                Phrase lastTwo = new Phrase(List.of(sndLast, last));
                ContextFreeRule lastTwoRule = new ContextFreeRule(r.getName() + "_c" + (rhs.size() - 2), connector, lastTwo);
                changes.addition(lastTwoRule);
            }
        }
    }

    private void copyRule(NonTerminal newLHS, ContextFreeRule rule) {
        changes.addition(new ContextFreeRule(rule.getName(), newLHS, rule.getRHS()));
    }

    /**
     * When finding a Rule of form A -> B where A and B are Non-Terminals, kill that production and add all rules of form B -> ... to A
     * This might add a new unit rule to A if B itself has a unit rule (i.e. A -> B -> C  =>  A -> C)
     * This method should thus be called until no more changes are added ( = max chain length times)
     */
    private void killUnitRulesIteration() {
        for (ContextFreeRule r : originalRules) {
            NonTerminal lhs = r.getLHS();
            Phrase rhs = r.getRHS();

            HashMap<NonTerminal, ArrayList<NonTerminal>> chains = new HashMap<>();
            // Search for unit productions (e.g. A -> B)
            if (rhs.size() == 1 && rhs.get(0).getBuilder() instanceof NonTerminal rhsTerminal) {
                originalRules.stream()
                        .filter(rule -> rule.getLHS().equals(rhsTerminal))  // Find all rules of this terminal B
                        .forEach(rule -> copyRule(lhs, rule));   // Replace them by parent rule (B -> XY becomes A -> XY)
                changes.deletion(r);
            }
        }
    }
}
