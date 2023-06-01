package database;

import com.mongodb.MongoClient;;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import data.Row;
import database.controller.MongoDBController;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongoDB implements Database{

    private MongoClient connection;
    List<Row> rows;

    public MongoDB(){
        this.connection = MongoDBController.getConnection();
        rows = new ArrayList<>();
    }

    @Override
    public List<Row> getDataFromTable(String from) {

        Row row = new Row();
        MongoDatabase database = connection.getDatabase("bp_tim35");
        MongoCollection<Document> collection = database.getCollection("employees");
        FindIterable<Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
        connection.close();

        return null;
    }
}
