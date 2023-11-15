
# Phrase grammar sentence generator

This repository generates sentences based on a defined Phrase grammar.
In short, it calculates possible derivations for a start phrase and chooses the desired ones until all variables turn to non-terminals or no more rules can be applied.
The generated abstract sentence may then be instantiazied by replacing Non-Terminals with pre-defined words from the Dictionary.

Works with:
- context free grammars
- ~context sensitive grammars~ # Not supported by the full pipeline, but core derivation exist

# Generation process
  
The standard derivation process is defined in ExecutionPipeline.java it will
1. Build a possibility Tree, which outputs a number of Phrases (i.e. words still containing non-terminals)
2. Derive each of those phrases until the end (no more non-terminals / no rule applicable)
3. Map each of those end phrases to a number of data points (input-label-pairs)
4. Merge all those data points into a single file
5. Save this file on disk

Each of this step may be overridden by defining your own respective generators or redefining a new pipeline.

Via the configuration file (which is currently hardcoded as grammar_config_example.json) you may supply Settings, Grammars. A GenerationInstance (i.e. one file) will be generated for each Setting-Grammar-pair you define under "Outputs" within this file.

# Further notes

- The Generator works iteratively, enabling creating quite large data sets (A size of 100.000 is recommended)
- The Code included a CNF converter and a CFG parser, which you may want to employ
- ...
