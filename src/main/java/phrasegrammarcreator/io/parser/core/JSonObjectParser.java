package phrasegrammarcreator.io.parser.core;

import org.json.JSONObject;

public abstract class JSonObjectParser<T> extends JsonParser<JSONObject, T> {
    public abstract T parse(JSONObject object) throws Exception;
}
