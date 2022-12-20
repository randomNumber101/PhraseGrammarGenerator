package phrasegrammarcreator.io.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;
import org.json.JSONObject;
import org.json.JSONTokener;
import phrasegrammarcreator.io.parser.impl.ConfigParser;
import phrasegrammarcreator.main.Config;

import java.io.*;
import java.util.Set;

public class ConfigLoader {

    private final String CONFIG_NAME = "grammar_config.schema.json";
    private final JsonSchema configSchema;

    private final ObjectMapper mapper = new ObjectMapper();
    public ConfigLoader() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream configSchemaStream = classLoader.getResourceAsStream(CONFIG_NAME);
        JsonNode schemaNode = loadNode(configSchemaStream);
        configSchemaStream.close();

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(schemaNode));
        configSchema =  factory.getSchema(schemaNode);
    }

    private JsonNode loadNode(InputStream stream) throws IOException {
        return mapper.readTree(stream);
    }

    public Set<ValidationMessage> validate(File configFile) throws IOException {
        InputStream stream = getStream(configFile);
        JsonNode configNode = loadNode(stream);
        return configSchema.validate(configNode);
    }

    public Config forceParse(File configFile) throws Exception {
        InputStream stream = getStream(configFile);
        JSONTokener tokenizer = new JSONTokener(stream);
        JSONObject root = new JSONObject(tokenizer);
        stream.close();

        ConfigParser configParser = new ConfigParser();
        return configParser.parse(root);
    }

    public Config validateAndParse(File configFile) throws Exception {
        Set<ValidationMessage> validationMessages = validate(configFile);
        if (!validationMessages.isEmpty()) {
            throw new IOException(validationMessages.stream().toString());
        }
        else {
            return forceParse(configFile);
        }
    }

    private InputStream getStream(File file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return fileInputStream;
    }

}
