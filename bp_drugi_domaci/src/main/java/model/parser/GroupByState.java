package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.GroupByClause;

public class GroupByState extends ParserState{
    AbstractClause group_by = null;
    public AbstractClause process(String token, boolean next) {

        if(group_by == null)
            group_by = new GroupByClause();

        if(!token.equalsIgnoreCase("group_by") && !next && !token.matches("\\s+")){
            group_by.getParameters().add(token);
            System.out.println("param: " + token);
        }
        return returnClause(next, group_by);
    }

    private AbstractClause returnClause(boolean next, AbstractClause join){
        if(next)
            return join;
        return null;
    }
}
