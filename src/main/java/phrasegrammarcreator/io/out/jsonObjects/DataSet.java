package phrasegrammarcreator.io.out.jsonObjects;

import phrasegrammarcreator.util.futures.SimpleFuture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

public class DataSet {

    private SimpleFuture<MetaInformation> metaInformation;
    private Iterator<Datum> data;

    public DataSet(SimpleFuture<MetaInformation> information, Iterator<Datum> data) {
        this.metaInformation = information;
        this.data = data;
    }


    public MetaInformation getMetaInformation() {
        return metaInformation.get();
    }

    public Iterator<Datum> getData() {
        return data;
    }
}
