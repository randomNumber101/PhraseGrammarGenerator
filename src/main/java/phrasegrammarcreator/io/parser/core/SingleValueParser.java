package phrasegrammarcreator.io.parser.core;

public abstract class SingleValueParser<T> extends JsonParser<String, T>{
    @Override
    public abstract T parse(String jsonType) throws Exception;
}
