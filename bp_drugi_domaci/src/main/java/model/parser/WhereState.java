package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.OrderByClause;
import model.sql_abstraction.WhereClause;

public class WhereState extends ParserState{
    AbstractClause where = null;
    public AbstractClause process(String token, boolean next) {

        if(where == null)
            where = new WhereClause();

        if(!token.equalsIgnoreCase("where") && !next){
            where.getParameters().add(token);
            System.out.println("param: " + token);
        }
        return returnClause(next, where);
    }

    private AbstractClause returnClause(boolean next, AbstractClause join){
        if(next)
            return join;
        return null;
    }
}
