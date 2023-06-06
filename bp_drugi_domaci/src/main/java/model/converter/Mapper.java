package model.converter;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import model.sql.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapper {

    List<AbstractClause> clauses;
    String projection = "{}";
    String collection = "{}";
    String find = "{}";
    String sort = "{}";

    public Mapper(List<AbstractClause> clauses){
        this.clauses = clauses;
    }

    public HashMap<String, String> map(){

//        MongoDB mongoDB = new MongoDB();
//        MongoCursor<Document> cursor = null;

        HashMap<String, String> convertedParams = new HashMap<>();
        for(AbstractClause clause: clauses){

            if(clause instanceof SelectClause){
                ParameterConverter projectionConverter = new ProjectionConverter(clause);
                projection = projectionConverter.translate();
                convertedParams.put("projection", projection);
            }else if(clause instanceof FromClause){
                ParameterConverter collectionConverter = new CollectionConverter(clause);
                collection = collectionConverter.translate();
                convertedParams.put("collection",collection);
            }else if(clause instanceof WhereClause){
                ParameterConverter findConverter = new FindConverter(clause);
                find = findConverter.translate();
                convertedParams.put("find",find);
            }else if(clause instanceof OrderByClause){
                ParameterConverter sortConverter = new SortConverter(clause);
                sort = sortConverter.translate();
                convertedParams.put("sort",sort);
            }
        }
            return convertedParams;

//        cursor = mongoDB.getDatabase().getCollection(collection)
//                .find(Document.parse(find))
//                .projection(Document.parse(projection))
//                .sort(Document.parse(sort))
//                .iterator();


//         AggregationConverter aggregation = new AggregationConverter(clauses.get(0));
//         List<Document> stages = aggregation.translate(clauses);
//         cursor = mongoDB.getDatabase().getCollection(collection).aggregate(stages).iterator();

//         mongoDB.closeConnection();
//         while (cursor.hasNext()){
//             Document d = cursor.next();
//             System.out.println("STA JE OVO" + d.toJson());
//         }

//        AggregationConverter aggregation = new AggregationConverter(clauses.get(0));
//        List<Document> pipeline = aggregation.translate(clauses);
//        cursor = mongoDB.getDatabase().getCollection(collection).aggregate(pipeline).iterator();

//        return cursor;
    }

    /**
     * "[
     * { \"$match\":
     * { \"$or\": [
     *          { \"department_id\": { \"$gt\": 30 } },
     *          { \"manager_id\": { \"$lt\": 120 } }
     *          ]
     * } },
     * { \"$group\":
     *          { \"_id\":
     *          { \"department_id\": \"$department_id\", \"manager_id\": \"$manager_id\" },
     *          \"avgSalary\": { \"$avg\": \"$salary\" }
     *          }
     * },
     * { \"$sort\":
     *          { \"avgSalary\": 1, \"department_id\": 1 }
     * }
     * ]";
     * */

}
