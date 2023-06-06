package model.executor;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Executor {

    String projection="{}";
    String collection="{}";
    String find = "{}";
    String sort = "{}";
    public MongoCursor<Document> execute(HashMap<String, String> convertedParams){

        MongoDB mongoDB = new MongoDB();
        MongoCursor<Document> cursor = null;

        collection = convertedParams.getOrDefault("collection", "{}");

        find = convertedParams.getOrDefault("find", "{}");
        projection = convertedParams.getOrDefault("projection", "{}");
        sort = convertedParams.getOrDefault("sort", "{}");





        cursor = mongoDB.getDatabase().getCollection(collection)
                .find(Document.parse(find))
                .projection(Document.parse(projection))
                .sort(Document.parse(sort))
                .iterator();

        return cursor;
    }
}
