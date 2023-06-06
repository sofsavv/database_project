package model.converter;

import model.query.SQLquery;
import model.sql.AbstractClause;

import java.util.ArrayList;
import java.util.List;

public class ProjectionConverter extends ParameterConverter{

    public ProjectionConverter(SQLquery sqlQuery) {
        super(sqlQuery);
    }
    // db.restaurants.find({}).projection({name:1, cuisine:1, _id:0, stars:1})
    @Override
    public String translate() {

        String projection = "";
        List<String> parameters = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if(!clauseParams("select").isEmpty()){
            parameters = clauseParams("select");
        }

        for(String param: parameters){

            if(aggregation(param)) continue;
            if(param.equals("*")) break;

            sb.append(param);
            if(!param.equals(",")){
                sb.append(": 1");
            }
        }
        sb.append("}");
        projection = sb.toString();

        System.out.println("select: " + projection);

        return projection;
    }
}
