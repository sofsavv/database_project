package model.executor;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import model.converter.AggregationConverter;
import model.query.SQLquery;
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
        MongoCursor<Document> cursor;

        collection = convertedParams.getOrDefault("collection", "{}");
        find = convertedParams.getOrDefault("find", "{}");
        projection = convertedParams.getOrDefault("projection", "{}");
        sort = convertedParams.getOrDefault("sort", "{}");

        cursor = mongoDB.getDatabase().getCollection(collection)
                .find(Document.parse(find))
                .projection(Document.parse(projection))
                .sort(Document.parse(sort))
                .iterator();

        mongoDB.closeConnection();

        return cursor;
    }

    public MongoCursor<Document> executeAggregation(SQLquery sqlQuery){

        MongoDB mongoDB = new MongoDB();
        MongoCursor<Document> cursor;
        System.out.println("USAO U EXC");
        AggregationConverter aggregation = new AggregationConverter(sqlQuery);
        List<Document> pipeline = aggregation.translate(sqlQuery.getClauses());
        cursor = mongoDB.getDatabase().getCollection(collection).aggregate(pipeline).iterator();

        mongoDB.closeConnection();

        return cursor;
    }

}
