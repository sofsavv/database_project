package adapter;

import model.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MongoDBAdapter implements Adapter{

    private Query query;
    public MongoDBAdapter(Query query){
        this.query = query;
    }
    @Override
    public List<String> map() {

        List<String> mongoQ = new ArrayList<>();

        for(String s: query.getParameters()){

            if(s.equalsIgnoreCase("select")) mongoQ.add("project");
            else if(s.equalsIgnoreCase("from")) mongoQ.add("collection");
            else if(s.equalsIgnoreCase("where")) mongoQ.add("match");
            else if(s.equalsIgnoreCase("join")) mongoQ.add("lookup");
            else if(s.equalsIgnoreCase("group_by")) mongoQ.add("group");
            else if(s.equalsIgnoreCase("order_by")) mongoQ.add("sort");
            else if(s.equals("<")) mongoQ.add("$lt");
            else if(s.equals(">")) mongoQ.add("$gt");
            else if(s.equals(">=")) mongoQ.add("$gte");
            else if(s.equals("<=")) mongoQ.add("$lte");
            else if(s.equals("!=")) mongoQ.add("$ne");
            else if(s.equals("=")) mongoQ.add("$eq");
            else if(s.equalsIgnoreCase("asc")) mongoQ.add("1");
            else if(s.equalsIgnoreCase("dsc")) mongoQ.add("-1");
            else mongoQ.add(s);
        }
        return mongoQ;
    }

}
