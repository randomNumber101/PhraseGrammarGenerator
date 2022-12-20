package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;

public class ContextFreeRule extends Rule<NonTerminal, Phrase> {

    public ContextFreeRule(NonTerminal from, Phrase to) {
        super(from, to);
    }
}
