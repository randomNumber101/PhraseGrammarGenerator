package phrasegrammarcreator.main;

import phrasegrammarcreator.core.FormalGrammar;

public record GenerationInstance(FormalGrammar grammar, Settings settings, String outputName, boolean overwriteOld) {}
