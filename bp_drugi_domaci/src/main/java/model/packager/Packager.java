package model.packager;

import com.mongodb.client.MongoCursor;
import org.bson.Document;


public interface Packager {

    void pack(MongoCursor<Document> documents);
}
