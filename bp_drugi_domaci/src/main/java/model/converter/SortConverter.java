package model.converter;

import model.query.SQLquery;
import model.sql.AbstractClause;

import java.util.ArrayList;
import java.util.List;

public class SortConverter extends ParameterConverter{

    // ORDER BY column1 ASC, column2 DESC -> {column1: 1, column2: -1}
    public SortConverter(SQLquery sqlQuery) {
        super(sqlQuery);
    }

    @Override
    public String translate() {

        String sort = "";
        String alias;
        List<String> parameters = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        if(!clauseParams("order by").isEmpty()){
            parameters = clauseParams("order by");
        }

        sb.append("{");
        for(String param: parameters){

            if(aggregation(param)) {
                Aggregation agg = new Aggregation(param);
                alias = agg.getAlias();
                sb.append(alias);
                continue;
            }
            if(param.equalsIgnoreCase("asc")){
                sb.append(": 1");
            }else if(param.equalsIgnoreCase("dsc")){
                sb.append(": -1");
            }else sb.append(param);
        }
        sb.append("}");
        sort = sb.toString();
        System.out.println("order by: " + sort);
        return sort;
    }
}
