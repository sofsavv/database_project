package database;


import com.mongodb.client.MongoDatabase;
import data.Row;

import java.util.List;

public interface Database {
    List<Row> getDataFromTable(String from);
    MongoDatabase getDatabase();
}
