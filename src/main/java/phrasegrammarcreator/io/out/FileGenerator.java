package phrasegrammarcreator.io.out;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.GenerationInstance;
import phrasegrammarcreator.main.Settings;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class FileGenerator {

    public static void save(GenerationInstance generation, DataSet dataSet) {

        String outputDir = generation.settings().outputDir();



        JsonFactory factory = new JsonFactory();
        File outputFile = new File(outputDir + File.separator + generateName(generation, dataSet));
        outputFile.getParentFile().mkdirs();
        if (outputFile.exists() && !generation.overwriteOld())
            outputFile = new File(outputDir + File.separator + generateStampedName(generation, dataSet));

        try(OutputStream out = new FileOutputStream(outputFile)) {
            JsonGenerator generator = factory.createGenerator(out, JsonEncoding.UTF8);
            generator.setCodec(new ObjectMapper());

            generator.writeStartObject();

            // Write data iterable
            generator.writeFieldName("data");
            generator.writeStartArray();
            for (Iterator<Datum> it = dataSet.getData(); it.hasNext(); ) {
                Datum datum = it.next();
                generator.writeObject(datum);
            }
            generator.writeEndArray();

            // Need to write meta at end when all data has been processed
            generator.writeObjectField("meta", dataSet.getMetaInformation());

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
