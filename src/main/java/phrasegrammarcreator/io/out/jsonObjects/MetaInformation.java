package phrasegrammarcreator.io.out.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MetaInformation {

    private String dataName;

    private String desciption;
    private String task;
    private long randomSeed;

    private Map<String, Object> stats;


    public MetaInformation(String dataName, String description, String task, long randomSeed) {
        this.dataName = dataName;
        this.desciption = description;
        this.task = task;
        this.randomSeed = randomSeed;
    }

    public MetaInformation(String dataName, String description, String task, long randomSeed, Map<String, Object> stats) {
        this.dataName = dataName;
        this.desciption = description;
        this.task = task;
        this.randomSeed = randomSeed;
        this.stats = stats;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDesciption() {
        return desciption;
    }

    public String getTask() {
        return task;
    }

    public long getRandomSeed() {
        return randomSeed;
    }
}
