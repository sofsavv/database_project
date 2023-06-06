package model.parser;

import model.sql.AbstractClause;
import model.sql.GroupByClause;

public class GroupByState extends ParserState{
    AbstractClause group_by = null;
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(group_by == null)
            group_by = new GroupByClause();

        if(!token.equalsIgnoreCase("group_by") && !next && !token.matches("\\s+")){
            group_by.getParameters().add(token);
//            System.out.println("param: " + token);
        }
        return returnClause(next, group_by);
    }

}
