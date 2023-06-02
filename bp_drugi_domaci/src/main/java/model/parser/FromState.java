package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.FromClause;

public class FromState extends ParserState{
    @Override
    public AbstractClause process(String token) {
        // jer posle from moze samo jedna tabela
        AbstractClause from = new FromClause();
        from.getParameters().add(token);
        return from;
    }
}
