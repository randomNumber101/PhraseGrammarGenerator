package phrasegrammarcreator.core.derive.possibilities;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.possibilities.tree.ProductPossibilities;
import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.core.phrases.words.generate.AllCombinationsBracketedGenerator;
import phrasegrammarcreator.io.out.jsonObjects.Datum;

public class PossibilitiesGenerator {

    CfRuleContainer rc;
    ProductPossibilities root;

    int CAP = 10000;
    int DEPTH_CAP = 20;

    public PossibilitiesGenerator(FormalGrammar grammar, Phrase start) {
        rc = new CfRuleContainer(grammar);
        root = new ProductPossibilities(rc, start);

        long currentPossibilities = 0;
        int depth = 0;
        do {
            root.calculateNext();
            printInfo();
            currentPossibilities = root.getCount();
        }
        while(currentPossibilities < CAP && depth++ < DEPTH_CAP);

        System.out.println("Ps: " + root.getCount());

        for (Phrase p : root) {
            DerivationNode dummy = new DerivationNode(p, null);
            if (EndPhrase.validate(grammar, dummy)) {
                EndPhrase ep = EndPhrase.ofPhrase(grammar, dummy);
                Datum datum = new AllCombinationsBracketedGenerator().generate(ep).get(0);
                System.out.printf("{\n\t input : %s \n\t label: %s \n}", datum.input, datum.label);
            }
        }
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


}
