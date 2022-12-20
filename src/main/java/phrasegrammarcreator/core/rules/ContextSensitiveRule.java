package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrase;

public class ContextSensitiveRule extends Rule<Phrase, Phrase> {
    public ContextSensitiveRule(Phrase from, Phrase to) {
        super(from, to);
    }
}
