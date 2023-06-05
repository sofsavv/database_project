package model.converter;

import model.sql_abstraction.AbstractClause;

public interface ParameterConverter {

    public String translate(AbstractClause clause);
}
