package model.parser;

import model.sql_abstraction.AbstractClause;

public abstract class ParserState {

    public abstract AbstractClause process(String token, boolean next);

}
