package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.VariableInstance;
import phrasegrammarcreator.io.console.info.DerivationTreeInfo;
import phrasegrammarcreator.io.console.info.GrammarInfo;
import phrasegrammarcreator.io.out.FileGenerator;
import phrasegrammarcreator.io.parser.ConfigLoader;
import phrasegrammarcreator.main.pipeline.ExecutionPipeline;

import java.io.File;
import java.net.URI;

public class Application {

    public static void main(String[] args){
        Application application = new Application();
        application.start();
    }


    public void start() {
        try {
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
            FileGenerator.save(c.getInstances().get(0).settings().outputDir(), pipeline.apply(c.getInstances().get(0)));

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

        //printStream(file);

        Config config = configLoader.validateAndParse(new File(configUri));
        return config;
    }
}
