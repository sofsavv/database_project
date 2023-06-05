package model.converter;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import model.sql_abstraction.*;
import org.bson.Document;

import java.util.List;

public class Mapper {

    List<AbstractClause> clauses;
    String projection;
    String collection;
    String find;
    String sort;

    public Mapper(List<AbstractClause> clauses){
        this.clauses = clauses;
    }

    public MongoCursor<Document> map(){

        MongoDB mongoDB = new MongoDB();
        MongoCursor<Document> cursor = null;

        for(AbstractClause clause: clauses){

            if(clause instanceof SelectClause){
                ParameterConverter projection = new ProjectionConverter(clause);
                projection.translate();
            }else if(clause instanceof FromClause){
                ParameterConverter collection = new CollectionConverter(clause);
                collection.translate();
            }else if(clause instanceof WhereClause){
                ParameterConverter find = new FindConverter(clause);
                find.translate();
            }else if(clause instanceof OrderByClause){
                ParameterConverter sort = new SortConverter(clause);
                sort.translate();
            }

        }

//        cursor = mongoDB.getDatabase().getCollection(collection)
//                .find(Document.parse(find))
//                .projection(Document.parse(projection))
//                .sort(Document.parse(sort))
//                .iterator();

        return cursor;
    }

}
