package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.JoinClause;

public class JoinState extends ParserState{
    AbstractClause join = null;
    @Override
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(join == null)
            join = new JoinClause();

        if(!token.equalsIgnoreCase("join") && !next && !token.matches("\\s+")){
            join.getParameters().add(token);
//            System.out.println("param: " + token);
        }
        return returnClause(next, join);
    }

}
