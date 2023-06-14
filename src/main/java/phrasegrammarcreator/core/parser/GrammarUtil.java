package phrasegrammarcreator.core.parser;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.rules.ContextFreeRule;

import java.util.ArrayList;
import java.util.List;

public class GrammarUtil {

    public static boolean isTerminal(VariableInstance vi) {
        return vi.getBuilder().getType().equals(Variable.Type.TERMINAL);
    }

    public static boolean isNonTerminal(VariableInstance vi) {
        return vi.getBuilder().getType().equals(Variable.Type.NON_TERMINAL);
    }
}
