package phrasegrammarcreator.io.out;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.GenerationInstance;
import phrasegrammarcreator.main.Settings;
import phrasegrammarcreator.util.IteratorTools;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileGenerator {

    public static void save(GenerationInstance generation, DataSet dataSet) {

        int maxCount = generation.settings().possibilityCap();
        String outputDir = generation.settings().outputDir();
        List<Datum> data = IteratorTools.loadAll(dataSet.getData());

        JsonFactory factory = new JsonFactory();
        File outputFile = new File(outputDir + File.separator + generateName(generation, dataSet));
        outputFile.getParentFile().mkdirs();
        if (outputFile.exists() && !generation.overwriteOld())
            outputFile = new File(outputDir + File.separator + generateStampedName(generation, dataSet));

        try(OutputStream out = new FileOutputStream(outputFile)) {
            JsonGenerator generator = factory.createGenerator(out, JsonEncoding.UTF8);
            ObjectMapper mapper = new ObjectMapper();
            generator.setCodec(mapper);

            generator.writeStartObject();
            generator.writeRaw("\n");

            // Write meta Information
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            generator.writeObjectField("meta", dataSet.getMetaInformation());

            // Write data (Can be modified to instead write data iteratively)
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            generator.writeFieldName("data");
            generator.writeStartArray();
            for (Datum datum : data) {
                generator.writeRaw("\n\t");
                generator.writeObject(datum);
            }
            generator.writeEndArray();

            // Close
            generator.writeEndObject();
            generator.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String generateName(GenerationInstance generation, DataSet set) {
        String name = generation.outputName();
        String seed = String.valueOf(set.getMetaInformation().getRandomSeed());

        return name + "_" + seed + ".json";
    }

    public static String generateStampedName(GenerationInstance generation, DataSet set) {
        String name = generation.outputName();
        String seed = String.valueOf(set.getMetaInformation().getRandomSeed());

        return name + "_" + seed + "_" + getTimeStamp() +".json";
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
