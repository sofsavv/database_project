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

    public boolean aggregation(String str){

        return (str.startsWith("avg(") && str.endsWith(")"))
                || (str.startsWith("sum(") && str.endsWith(")"))
                || (str.startsWith("count(") && str.endsWith(")"))
                || (str.startsWith("min(") && str.endsWith(")"))
                || (str.startsWith("max(") && str.endsWith(")"));

    }

}
