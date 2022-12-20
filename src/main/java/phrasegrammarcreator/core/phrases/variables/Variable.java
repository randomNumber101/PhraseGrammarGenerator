package phrasegrammarcreator.core.phrases.variables;

import phrasegrammarcreator.core.phrases.Phrasable;
import phrasegrammarcreator.core.phrases.Phrase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class Variable implements Phrasable {

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }

    public enum Type {
        NON_TERMINAL,
        TERMINAL
    }
    private ArrayList<Variable> derivedFrom;


    private String regex;
    private String name;


    public Variable(String regex) {
        this(regex, regex);
    }

    public Variable(String regex, String name) {
        this.name = name;
        this.regex = regex;
    }

    public String toString() {
        return name;
    }

    public abstract Type getType();
    public abstract VariableInstance createInstance(Phrase phrase);


    public static Variable ofRegex(Type type, Vocabulary dict, String regex, @Nullable String name) {
        Variable result = dict.getVariable(regex);
        if (result != null)
            return result;

        Variable var = switch (type) {
                case TERMINAL -> (name == null)? new Terminal(regex) : new Terminal(regex, name);
                case NON_TERMINAL -> (name == null)? new NonTerminal(regex) : new NonTerminal(regex, name);
                default -> null;
        };
        dict.registerVariable(regex, var);
        return var;
    }


}
