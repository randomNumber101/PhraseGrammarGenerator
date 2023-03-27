package phrasegrammarcreator.io.out.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Datum {
    public final String i;
    public final String l;

    public Datum(String input, String label) {
        this.i = input;
        this.l = label;
    }
}
