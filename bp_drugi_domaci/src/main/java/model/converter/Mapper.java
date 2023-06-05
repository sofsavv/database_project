package model.converter;

import com.mongodb.client.MongoCursor;
import database.MongoDB;
import model.sql_abstraction.*;
import org.bson.Document;

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

    public MongoCursor<Document> map(){

        MongoDB mongoDB = new MongoDB();
        MongoCursor<Document> cursor = null;

        for(AbstractClause clause: clauses){

            if(clause instanceof SelectClause){
                ParameterConverter projectionConverter = new ProjectionConverter(clause);
                projection = projectionConverter.translate();
            }else if(clause instanceof FromClause){
                ParameterConverter collectionConverter = new CollectionConverter(clause);
                collection = collectionConverter.translate();
            }else if(clause instanceof WhereClause){
                ParameterConverter findConverter = new FindConverter(clause);
                find = findConverter.translate();
            }else if(clause instanceof OrderByClause){
                ParameterConverter sortConverter = new SortConverter(clause);
                sort = sortConverter.translate();
            }

        }

        cursor = mongoDB.getDatabase().getCollection(collection)
                .find(Document.parse(find))
                .projection(Document.parse(projection))
                .sort(Document.parse(sort))
                .iterator();

        while (cursor.hasNext()){
            Document d = cursor.next();
            System.out.println("STA JE OVO" + d.toJson());
        }

        return cursor;
    }

}
