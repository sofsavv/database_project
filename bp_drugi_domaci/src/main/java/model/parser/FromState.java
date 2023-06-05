package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.FromClause;

public class FromState extends ParserState{

    AbstractClause from;
    @Override
    public AbstractClause process(String token, boolean next, boolean bracket) {

        // moze po zarezu i onda je isto kao da pise sa join gde su iste kolone tabela

        if(from == null)
            from = new FromClause();

        if(!token.equalsIgnoreCase("from") && !next && !token.matches("\\s+")){
            from.getParameters().add(token);
//            System.out.println("param: " + token);
        }
        return returnClause(next, from);
    }

}
