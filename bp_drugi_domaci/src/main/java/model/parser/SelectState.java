package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.SelectClause;

public class SelectState extends ParserState{

    AbstractClause select = null;

    @Override
    public AbstractClause process(String token, boolean next) {

        if(select == null)
            select = new SelectClause();

        if (token.contains(",")) {
            String param = token.substring(0, token.indexOf(","));
            select.getParameters().add(param);
            System.out.println("param: " + param);

        }else if(!token.equalsIgnoreCase("select") && !next){
            select.getParameters().add(token);
            System.out.println("param: " + token);
        }
        return returnClause(next, select);
    }
    private AbstractClause returnClause(boolean next, AbstractClause join){
        if(next)
            return join;
        return null;
    }

}
