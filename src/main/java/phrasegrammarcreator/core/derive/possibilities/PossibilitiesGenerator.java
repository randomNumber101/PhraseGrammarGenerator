package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.tree.ProductPossibilities;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.main.GenerationInstance;

import java.util.Iterator;
import java.util.function.Function;

public class PossibilitiesGenerator implements Function<GenerationInstance, Iterator<Phrase>> {

    CfRuleContainer rc;
    ProductPossibilities root;

    int CAP = 100000;
    int DEPTH_CAP = 20;

    private int depth;

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start) {
        rc = grammar.getRuleContainer();
        root = new ProductPossibilities(rc, start);
        depth = 0;
    }

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start, int countCap, int depthCap) {
        rc = grammar.getRuleContainer();
        root = new ProductPossibilities(rc, start);
        depth = 0;
        CAP = countCap;
        DEPTH_CAP = depthCap;
    }

    private void printInfo() {
        System.out.println("Ps: " + root.getCount());
        for (Phrase p : root)
            System.out.print(p + ", ");
        System.out.println();
    }

    private void printInstancePath(VariableInstance instance) {
        VariableInstance current = instance;
        do {
            System.out.print(current + " <- ");
            current = current.getDerivedFrom();
        }
        while (current != null);
        System.out.println("root");
    }


    @Override
    public Iterator<Phrase> apply(GenerationInstance generationInstance) {
        long currentPossibilities = 0;
        do {
            root.calculateNext();
            printInfo();
            currentPossibilities = root.getCount();
        }
        while(currentPossibilities < CAP && depth++ < DEPTH_CAP);
        return root.iterator();
    }

    public long getCurrentCount() {
        return root.getCount();
    }

    public int getCurrentDepth() {
        return depth;
    }
}
