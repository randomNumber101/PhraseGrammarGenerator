package phrasegrammarcreator.io.parser.impl;

import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.io.parser.core.SingleValueParser;

public class NonTerminalParser extends SingleValueParser<NonTerminal> {
    private Vocabulary vocabulary;

    public NonTerminalParser(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }
    @Override
    public NonTerminal parse(String regex) throws Exception {
        return (NonTerminal) Variable.ofRegex(Variable.Type.NON_TERMINAL, vocabulary, regex, null);
    }
}
