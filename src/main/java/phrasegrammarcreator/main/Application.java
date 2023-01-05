package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.io.parser.ConfigLoader;
import phrasegrammarcreator.io.print.info.DerivationTreeInfo;
import phrasegrammarcreator.io.print.info.GrammarInfo;

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
            for (int i = 0; i < 32; i++, grammar.next()){}


            DerivationTreeInfo treeInfo = new DerivationTreeInfo(System.out, grammar.getDerivationTree());
            treeInfo.printInfo();
            grammarInfo.printPossibleDerivations();
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
