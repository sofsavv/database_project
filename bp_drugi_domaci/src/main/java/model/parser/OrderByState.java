package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.OrderByClause;

public class OrderByState extends ParserState{

    AbstractClause order_by = null;
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(order_by == null)
            order_by = new OrderByClause();

        if(!token.equalsIgnoreCase("order_by") && !next && !token.matches("\\s+")){
            order_by.getParameters().add(token);
            System.out.println("param: " + token);
        }
        return returnClause(next, order_by);
    }

}
