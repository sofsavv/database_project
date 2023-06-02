package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.SelectClause;

public class SelectState extends ParserState{

    AbstractClause select = null;

    @Override
    public void process(String token) {

        if(select == null)
            select = new SelectClause();

        if (token.contains(",")) {
            String[] columns = token.split(",");

            for (String c : columns) {
                String trimmedColumn = c.trim();
                select.getParameters().add(trimmedColumn);
            }
        }else if(token.equals("*")){
            select.getParameters().add(token);

        } else {
            String trimmedColumn = token.trim();
            select.getParameters().add(trimmedColumn);
        }

    }

}
