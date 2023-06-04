package model.parser;

import model.sql_abstraction.AbstractClause;

public abstract class ParserState {

    public abstract AbstractClause process(String token, boolean next, boolean bracket);

    protected AbstractClause returnClause(boolean next, AbstractClause clause){
        if(next)
            return clause;
        return null;
    }
    protected boolean isAggregation(String param){
        return (param.contains("avg(") && param.endsWith(")"))
                || (param.contains("sum(") && param.endsWith(")"))
                || (param.contains("min(") && param.endsWith(")"))
                || (param.contains("max(") && param.endsWith(")"))
                || (param.contains("count(") && param.endsWith(")"));
    }

}
