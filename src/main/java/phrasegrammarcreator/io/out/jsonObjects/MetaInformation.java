package phrasegrammarcreator.io.out.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MetaInformation {

    private String dataName;

    private String desciption;
    private String task;
    private long randomSeed;


    public MetaInformation(String dataName, String desciption, String task, long randomSeed) {
        this.dataName = dataName;
        this.desciption = desciption;
        this.task = task;
        this.randomSeed = randomSeed;
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
