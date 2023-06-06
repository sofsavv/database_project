package model.converter;

import model.query.SQLquery;
import model.sql.AbstractClause;
import model.sql.FromClause;

public class CollectionConverter extends ParameterConverter{

    public CollectionConverter(SQLquery sqlQuery) {
        super(sqlQuery);
    }

    @Override
    public String translate() {

        String collection = "";
        if(!clauseParams("from").isEmpty()){
            collection = clauseParams("from").get(0);
        }
        System.out.println("from: " + collection);
        return collection;
    }
}
