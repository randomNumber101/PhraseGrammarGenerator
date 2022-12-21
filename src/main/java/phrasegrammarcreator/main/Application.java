package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;
import phrasegrammarcreator.io.parser.ConfigLoader;
import phrasegrammarcreator.io.print.info.GrammarInfo;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

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
    private static void printStream(InputStream stream) throws IOException {
        InputStreamReader streamReader =
                new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
