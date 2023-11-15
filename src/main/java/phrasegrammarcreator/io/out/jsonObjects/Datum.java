package phrasegrammarcreator.io.out.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Datum {
    public final String i;
    public final String l;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public HashMap<String, Integer> stats = null;

    public Datum(String input, String label) {
        this.i = input;
        this.l = label;
    }

    public Datum(String input, String label, HashMap<String, Integer> stats) {
        this.i = input;
        this.l = label;
        this.stats = new HashMap<>();
        this.stats.putAll(stats);
    }
}
