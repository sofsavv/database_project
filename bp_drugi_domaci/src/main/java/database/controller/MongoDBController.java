package database.controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import utils.Constants;

import java.util.Arrays;

public class MongoDBController {

    private static String user = Constants.MONGO_USERNAME;
    private static String database = Constants.MONGO_DATABASE;
    private static String password = Constants.MONGO_PASSWORD;
    private static String ip = Constants.MONGO_IP;
    private static int port = 27017;

    public static MongoClient getConnection(){

        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress(ip, port), Arrays.asList(credential));

        System.out.println ("Mongo Database connection established");

        return mongoClient;

    }
}
