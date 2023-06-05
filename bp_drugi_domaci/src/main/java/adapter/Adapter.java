package adapter;

import com.mongodb.client.MongoCursor;
import model.sql_abstraction.AbstractClause;
import org.bson.Document;

public interface Adapter {
    public MongoCursor<Document> getMongoQuery(AbstractClause clause);
}
