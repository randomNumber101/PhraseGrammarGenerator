package phrasegrammarcreator.main;

import phrasegrammarcreator.core.CNFGrammar;
import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.parser.CYKParser;
import phrasegrammarcreator.io.console.info.GrammarInfo;
import phrasegrammarcreator.io.out.FileGenerator;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.parser.ConfigLoader;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.List;

public class Application {

    public static void main(String[] args){
        Application application = new Application();
        application.start();
    }


    public void start() {
        try {
            long startTime = System.currentTimeMillis();
            Config c = loadConfig();
            FormalGrammar grammar = c.getGrammarList().get(0);

            GrammarInfo grammarInfo = new GrammarInfo(System.out, grammar);
            grammarInfo.printInfo();

            /*


            for (int i = 0; i < 32; i++, grammar.next()){}


            DerivationTreeInfo treeInfo = new DerivationTreeInfo(System.out, grammar.getDerivationTree());
            treeInfo.printInfo();
            //grammarInfo.printPossibleDerivations();

            Phrase headPhrase = grammar.getDerivationTree().getHead().getData();
            System.out.println(headPhrase.toString(" "));
            VariableInstance current = headPhrase.get(0);
            do {
                System.out.print(current + " <- ");
                current = current.getDerivedFrom();
            }
            while (current != null);
            System.out.println("root");

            */


            //PossibilitiesGenerator generator = new PossibilitiesGenerator(grammar, grammar.getStartPhrase());

            //EndPhrase.ofPhrase(grammar, );

            ExecutionPipeline pipeline = new ExecutionPipeline();

            //String parseTestSentence00 = "you both had how last reserve less than number how most that .";
            //String parseTestSentence01 = "i need a flight from charlotte to las vegas that makes a stop in saint louis .";
            //String parseTestSentence2 = "Toni sagt ,_dass Toni sagt ,_dass Toni sagt ,_dass Toni sagt ,_dass es_morgen_schneit";
            //CNFGrammar cnfGrammar = new CNFGrammar(c.getGrammarList().get(0));
            //CYKParser cykParser = new CYKParser(cnfGrammar);
            //System.out.print(cykParser.parse(parseTestSentence01));

            List<DataSet> outputs = c.getInstances().stream().filter(FileGenerator::shouldGenerate).map(pipeline).toList();
            Desktop.getDesktop().open(new File(c.getSettings().get(0).outputDir()));

            long generationTime = (System.currentTimeMillis() - startTime);
            System.out.println("Finished. Took in seconds: " + generationTime / (1000));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Config loadConfig() throws Exception {
        ConfigLoader configLoader = new ConfigLoader();
        URI configUri = Thread.currentThread()
                .getContextClassLoader()
                .getResource("grammar_config_example.json")
                .toURI();

        return configLoader.validateAndParse(new File(configUri));
    }
}
