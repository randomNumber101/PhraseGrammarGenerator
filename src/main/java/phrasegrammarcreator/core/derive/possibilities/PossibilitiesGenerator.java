package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;

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
    }

    private void printInfo() {
        System.out.println("Ps: " + root.getCount());
        for (Phrase p : root)
            System.out.print(p + ", ");
        System.out.println();
    }


}
