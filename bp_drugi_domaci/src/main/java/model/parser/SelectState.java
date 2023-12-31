package model.parser;

import model.sql.AbstractClause;
import model.sql.SelectClause;

public class SelectState extends ParserState{

    AbstractClause select = null;

    @Override
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(select == null)
            select = new SelectClause();

        if(!token.equalsIgnoreCase("select") && !next && !token.matches("\\s+")){
            String param = token.trim();
            select.getParameters().add(param);
//            System.out.println("param: " + param);
        }
        return returnClause(next, select);
    }

}
