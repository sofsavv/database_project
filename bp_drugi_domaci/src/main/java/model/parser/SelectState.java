package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.SelectClause;

public class SelectState extends ParserState{

    AbstractClause select = null;

    @Override
    public AbstractClause process(String token, boolean next) {

        if(select == null)
            select = new SelectClause();

        if(!token.equalsIgnoreCase("select") && !next && !token.matches("\\s+")){
            String param = token.trim();
            select.getParameters().add(param);
            System.out.println("param: " + param);
        }
        return returnClause(next, select);
    }
    private AbstractClause returnClause(boolean next, AbstractClause join){
        if(next)
            return join;
        return null;
    }

}
