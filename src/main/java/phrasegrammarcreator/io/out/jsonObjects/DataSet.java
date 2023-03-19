package phrasegrammarcreator.io.out.jsonObjects;

import phrasegrammarcreator.core.phrases.EndPhrase;
import phrasegrammarcreator.io.out.DatumGenerator;

import java.util.ArrayList;

public class DataSet {

    private MetaInformation metaInformation;
    private ArrayList<Datum> data;

    public DataSet(MetaInformation metaInformation, ArrayList<Datum> data) {
        this.metaInformation = metaInformation;
        this.data = data;
    }

    public DataSet(MetaInformation information, ArrayList<EndPhrase> phrases, DatumGenerator generator) {
        ArrayList<Datum> data = new ArrayList<>(phrases.stream().map(generator).toList());

        this.metaInformation = information;
        this.data = data;
    }


    public MetaInformation getMetaInformation() {
        return metaInformation;
    }

    public ArrayList<Datum> getData() {
        return data;
    }
}
