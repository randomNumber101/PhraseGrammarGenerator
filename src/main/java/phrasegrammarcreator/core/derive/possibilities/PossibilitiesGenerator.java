package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.possibilities.tree.ProductPossibilities;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;

public class PossibilitiesGenerator {

    CfRuleContainer rc;
    ProductPossibilities root;

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start) {
        rc = new CfRuleContainer(grammar);
        root = new ProductPossibilities(rc, start);
        printInfo();
        root.calculateNext();
        printInfo();
        root.calculateNext();
        printInfo();
        root.calculateNext();
        System.out.println("Ps: " + root.getCount());
        Phrase first = root.iterator().next();
        System.out.println(first.toString(" "));
        VariableInstance current = first.get(0);
        do {
            System.out.print(current + " <- ");
            current = current.getDerivedFrom();
        }
        while (current != null);
        System.out.println("root");

    }

    private void printInfo() {
        System.out.println("Ps: " + root.getCount());
        for (Phrase p : root)
            System.out.print(p + ", ");
        System.out.println();
    }


}
