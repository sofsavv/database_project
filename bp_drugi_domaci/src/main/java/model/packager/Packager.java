package model.packager;

import com.mongodb.client.MongoCursor;


public interface Packager {

    void pack(MongoCursor<org.bson.Document> documents);
}
