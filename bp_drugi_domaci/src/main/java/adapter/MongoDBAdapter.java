package adapter;

import com.mongodb.client.MongoCursor;
import model.sql_abstraction.AbstractClause;
import org.bson.Document;

public class MongoDBAdapter implements Adapter {
    @Override
    public MongoCursor<Document> getMongoQuery(AbstractClause clause) {

        return null;
    }
}
