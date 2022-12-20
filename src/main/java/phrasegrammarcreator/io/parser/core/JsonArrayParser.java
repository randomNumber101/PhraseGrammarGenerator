package phrasegrammarcreator.io.parser.core;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayParser<O, T> extends JsonParser<JSONArray, List<T>> {

    private final JsonParser<O,T> elementParser;

    public JsonArrayParser(JsonParser<O,T> elementParser) {
        this.elementParser = elementParser;
    }
    public List<T> parse(JSONArray jsonArray) throws Exception {
        ArrayList<T> out = new ArrayList<>();
        for (Object obj : jsonArray ) {
            O elem = (O) obj;
            out.add(elementParser.parse(elem));
        }
        return out;
    }
}
