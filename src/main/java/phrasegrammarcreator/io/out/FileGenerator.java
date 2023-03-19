package phrasegrammarcreator.io.out;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import phrasegrammarcreator.io.out.jsonObjects.DataSet;
import phrasegrammarcreator.io.out.jsonObjects.Datum;
import phrasegrammarcreator.main.Settings;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileGenerator {



    public static void save(DataSet dataSet) {
        JsonFactory factory = new JsonFactory();
        File outputFile = new File(Settings.getInstance().outputDir + File.separator + generateName(dataSet));
        outputFile.getParentFile().mkdirs();
        if (outputFile.exists())
            outputFile = new File(Settings.getInstance().outputDir + File.separator + generateStampedName(dataSet));

        try(OutputStream out = new FileOutputStream(outputFile)) {
            JsonGenerator generator = factory.createGenerator(out, JsonEncoding.UTF8);

            generator.writeStartObject();
                generator.writeObjectField("meta", dataSet.getMetaInformation());
                generator.writeFieldName("data");
                    generator.writeStartArray();
                        for (Datum datum : dataSet.getData())
                            generator.writeObject(datum);
                    generator.writeEndArray();
            generator.writeEndObject();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static String generateName(DataSet set) {
        String name = set.getMetaInformation().getDataName();
        String seed = String.valueOf(set.getMetaInformation().getRandomSeed());

        return name + "_" + seed + ".json";
    }

    public static String generateStampedName(DataSet set) {
        String name = set.getMetaInformation().getDataName();
        String seed = String.valueOf(set.getMetaInformation().getRandomSeed());

        return name + "_" + seed + "_" + getTimeStamp() +".json";
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}
