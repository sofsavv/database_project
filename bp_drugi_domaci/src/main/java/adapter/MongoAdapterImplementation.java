package adapter;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class MongoAdapterImplementation implements MongoAdapter{
    @Override
    public MongoCursor<Document> getMongoQuery() {
        return null;
    }
}
