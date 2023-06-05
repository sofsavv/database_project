package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.WhereClause;

public class WhereState extends ParserState{
    AbstractClause where = null;
    StringBuilder sb = new StringBuilder();
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(where == null)
            where = new WhereClause();

        if(!token.equalsIgnoreCase("where") && bracket){

            if(token.contains(")") && !isAggregation(token)){
                sb.append(token);
                where.setTemp(sb.toString());
                where.getParameters().add(where.getTemp());
                System.out.println("temp na kraju: "+where.getTemp());
                where.setTemp("");
                sb = new StringBuilder();
                bracket = false;
            }else{
                sb.append(token);
                sb.append(" ");
            }

        }else if(!token.equalsIgnoreCase("where") && !next && !token.matches("\\s+")){
            where.getParameters().add(token);
//            System.out.println("param: " + token);
        }
//        else if(token.equalsIgnoreCase("where")){
//            next = false;
//        }
        return returnClause(next, where);
    }

}
