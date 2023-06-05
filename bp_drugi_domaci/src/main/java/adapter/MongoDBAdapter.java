package adapter;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class MongoDBAdapter implements Adapter {
    @Override
    public MongoCursor<Document> getMongoQuery() {
        return null;
    }
}
