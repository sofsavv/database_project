package adapter;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

public interface MongoAdapter {
    public MongoCursor<Document> getMongoQuery();
}
