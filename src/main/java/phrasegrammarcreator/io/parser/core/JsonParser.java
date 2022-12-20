package phrasegrammarcreator.io.parser.core;

public abstract class JsonParser<J, O> {

    public abstract O parse(J jsonType) throws Exception;
}
