package model.converter;

import lombok.Getter;
import lombok.Setter;
import model.sql_abstraction.AbstractClause;

@Getter
@Setter
public abstract class ParameterConverter {

    private AbstractClause clause;

    public ParameterConverter(AbstractClause clause){
        this.clause = clause;
    }
    public abstract String translate();
}
