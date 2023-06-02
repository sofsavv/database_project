package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.SelectClause;

public class SelectState extends ParserState{

    AbstractClause select = null;

    @Override
    public AbstractClause process(String token) {

        //select name, surname from
        // select name
        //name,
        //surname
        if(select == null)
            select = new SelectClause();

        if (token.contains(",")) {
            String param = token.substring(0, token.indexOf(","));
            select.getParameters().add(param);

        }else {
            select.getParameters().add(token);
            return select;
        }
        return null;
    }

}
