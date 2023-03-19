package phrasegrammarcreator.main;

public class Settings {

    private static Settings settings;

    public String outputDir;

    public Settings(String outputDir) {
        this.outputDir = outputDir;
    }

    public static void setSettings(Settings s) {
        settings = s;
    }

    public static Settings getInstance() {
        return settings;
    }
}
