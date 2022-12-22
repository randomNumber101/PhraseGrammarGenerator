package phrasegrammarcreator.core.rules;


import phrasegrammarcreator.core.phrases.Phrase;

public class ContextSensitiveRule extends Rule<Phrase, Phrase> {
    public ContextSensitiveRule(String name, Phrase from, Phrase to) {
        super(name, from, to);
    }
}
