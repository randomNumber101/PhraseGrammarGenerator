package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.tree.ProductPossibilities;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.rules.CfRuleContainer;
import phrasegrammarcreator.main.GenerationInstance;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

public class PossibilitiesGenerator implements Function<GenerationInstance, Iterator<Phrase>> {

    CfRuleContainer rc;
    ProductPossibilities root;

    Random random;

    int CAP = 100000;
    int DEPTH_CAP = 20;

    private int depth;

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start) {
        rc = grammar.getRuleContainer();
        root = new ProductPossibilities(rc, start);
        depth = 0;
    }

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start, Random random, int countCap, int depthCap) {
        rc = grammar.getRuleContainer();
        root = new ProductPossibilities(rc, start);
        this.random = random;
        depth = 0;
        CAP = countCap;
        DEPTH_CAP = depthCap;
    }

    private void printInfo() {
        System.out.println("Ps: " + root.getNextCount());
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
        root.buildTreeTillCap(random, CAP, DEPTH_CAP);
        return root.iterator();
    }

    public long getCurrentCount() {
        return root.getNextCount();
    }

    public int getCurrentDepth() {
        return depth;
    }
}
