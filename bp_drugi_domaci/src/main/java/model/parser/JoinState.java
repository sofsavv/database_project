package model.parser;

import model.Operators;
import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.JoinClause;

public class JoinState extends ParserState{
    AbstractClause join = null;
    @Override
    public AbstractClause process(String token, boolean next) {

        if(join == null)
            join = new JoinClause();

        if(!token.equalsIgnoreCase("join") && !next){
            join.getParameters().add(token);
            System.out.println("param: " + token);
        }
        return returnClause(next, join);
    }

    private AbstractClause returnClause(boolean next, AbstractClause join){
        if(next)
            return join;
        return null;
    }

}
