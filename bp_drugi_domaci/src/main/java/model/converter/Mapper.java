package model.converter;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import model.query.SQLquery;
import model.sql.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapper {

    SQLquery sqlQuery;
    String projection = "{}";
    String collection = "{}";
    String find = "{}";
    String sort = "{}";

    public Mapper(SQLquery sqlQuery){
        this.sqlQuery = sqlQuery;
    }

    public HashMap<String, String> map(){

        HashMap<String, String> convertedParams = new HashMap<>();
        for(AbstractClause clause: sqlQuery.getClauses()){

            if(clause instanceof SelectClause){
                ParameterConverter projectionConverter = new ProjectionConverter(sqlQuery);
                projection = projectionConverter.translate();
                convertedParams.put("projection", projection);
            }else if(clause instanceof FromClause){
                ParameterConverter collectionConverter = new CollectionConverter(sqlQuery);
                collection = collectionConverter.translate();
                convertedParams.put("collection",collection);
            }else if(clause instanceof WhereClause){
                ParameterConverter findConverter = new FindConverter(sqlQuery);
                find = findConverter.translate();
                convertedParams.put("find",find);
            }else if(clause instanceof OrderByClause){
                ParameterConverter sortConverter = new SortConverter(sqlQuery);
                sort = sortConverter.translate();
                convertedParams.put("sort",sort);
            }
        }
            return convertedParams;
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
