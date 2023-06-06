package model.converter;

import lombok.Getter;
import lombok.Setter;
import model.query.SQLquery;
import model.sql.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ParameterConverter {

    private SQLquery sqlQuery;
    public ParameterConverter(SQLquery sqlQuery){
        this.sqlQuery = sqlQuery;
    }
    public abstract String translate();

    public boolean aggregation(String str){

        return (str.startsWith("avg(") && str.endsWith(")"))
                || (str.startsWith("sum(") && str.endsWith(")"))
                || (str.startsWith("count(") && str.endsWith(")"))
                || (str.startsWith("min(") && str.endsWith(")"))
                || (str.startsWith("max(") && str.endsWith(")"));

    }

    public List<String> clauseParams(String str){

        List<String> parameters = new ArrayList<>();
        for(AbstractClause clause: sqlQuery.getClauses()){
            if(clause.getKeyWord().equalsIgnoreCase(str)){
                parameters.addAll(clause.getParameters());
                return parameters;
            }
        }
        return parameters;
    }
}
