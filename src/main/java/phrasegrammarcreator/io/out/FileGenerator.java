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

    public static boolean shouldGenerate(GenerationInstance generation) {
        if (generation.overwriteOld())
            return true;
        File outputFile = new File(generation.settings().outputDir() + File.separator + generateName(generation));
        return (!outputFile.exists());
    }

    public static void save(GenerationInstance generation, DataSet dataSet) {

        int maxCount = generation.settings().possibilityCap();
        String outputDir = generation.settings().outputDir();

        JsonFactory factory = new JsonFactory();
        File outputFile = new File(outputDir + File.separator + generateName(generation));
        outputFile.getParentFile().mkdirs();
        if (outputFile.exists() && !generation.overwriteOld())
            outputFile = new File(outputDir + File.separator + generateStampedName(generation));

        try(OutputStream out = new FileOutputStream(outputFile)) {
            JsonGenerator generator = factory.createGenerator(out, JsonEncoding.UTF8);
            ObjectMapper mapper = new ObjectMapper();
            generator.setCodec(mapper);

            generator.writeStartObject();
            generator.writeRaw("\n");

            // Write data (Can be modified to instead write data iteratively)
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            generator.writeFieldName("data");
            generator.writeStartArray();
            for (Iterator<Datum> it = dataSet.getData(); it.hasNext(); ) {
                Datum datum = it.next();
                generator.writeRaw("\n\t");
                generator.writeObject(datum);
            }
            generator.writeEndArray();

            // Write meta Information
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            generator.writeObjectField("meta", dataSet.getMetaInformation());

            // Close
            generator.writeEndObject();
            generator.close();

            System.out.println("Saved dataset: " + dataSet.getMetaInformation().getDataName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String generateName(GenerationInstance generation) {
        String name = generation.outputName();
        String seed = String.valueOf(generation.settings().seed());

        return name + "_" + seed + ".json";
    }

    public static String generateStampedName(GenerationInstance generation) {
        String name = generation.outputName();
        String seed = String.valueOf(generation.settings().seed());

        return name + "_" + seed + "_" + getTimeStamp() +".json";
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
