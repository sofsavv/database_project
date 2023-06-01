package model;

import lombok.Getter;
import lombok.Setter;
import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.SelectClause;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Parser {

    List<AbstractClause> clauses;
    public Parser(){
        clauses = new ArrayList<>();
    }

    private List<AbstractClause> parse(String query){

        List<String> queries = new ArrayList<>();

        queries = List.of(query.split(" "));
        int i = 0;

        for(String q: queries){
            i++;
            if(q.equalsIgnoreCase("select")){
                AbstractClause selectClause = new SelectClause();

            }else if(q.equalsIgnoreCase("from")){

            }else if(q.equalsIgnoreCase("where")){

            }else if(q.equalsIgnoreCase("like")){

            }else if(q.equalsIgnoreCase("using")){

            }else if(q.equalsIgnoreCase("join")){

            }else if(q.equalsIgnoreCase("in")){

            }else if(q.equalsIgnoreCase("having")){

            }else if(q.equalsIgnoreCase("order") && queries.get(i).equalsIgnoreCase("by")){

            }

        }

        return null;
    }

}
