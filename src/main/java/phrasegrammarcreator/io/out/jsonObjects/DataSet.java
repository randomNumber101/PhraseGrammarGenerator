package phrasegrammarcreator.io.out.jsonObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSet {

    private MetaInformation metaInformation;
    private Iterator<Datum> data;

    public DataSet(MetaInformation information, Iterator<Datum> data) {
        this.metaInformation = information;
        this.data = data;
    }


    public MetaInformation getMetaInformation() {
        return metaInformation;
    }

    public Iterator<Datum> getData() {
        return data;
    }
}
