package adapter;

import model.sql.Query;

public class MongoDBAdapter implements Adapter{


    public MongoDBAdapter(Query query){

    }
    @Override
    public String getQuery() {
        return null;
    }

    // konstruktor uzima IQuery -> getPar()

}
