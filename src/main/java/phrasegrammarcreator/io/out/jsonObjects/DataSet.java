package phrasegrammarcreator.io.out.jsonObjects;

import java.util.ArrayList;
import java.util.List;

public class DataSet {

    private MetaInformation metaInformation;
    private List<Datum> data;

    public DataSet(MetaInformation metaInformation, ArrayList<Datum> data) {
        this.metaInformation = metaInformation;
        this.data = data;
    }

    public DataSet(MetaInformation information, List<Datum> data) {
        this.metaInformation = information;
        this.data = data;
    }


    public MetaInformation getMetaInformation() {
        return metaInformation;
    }

    public List<Datum> getData() {
        return data;
    }
}
