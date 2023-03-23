package phrasegrammarcreator.io.out.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Datum {
    public final String input;
    public final String label;

    public Datum(String input, String label) {
        this.input = input;
        this.label = label;
    }
}
