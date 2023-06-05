package model.validator;

import model.sql_abstraction.AbstractClause;

import java.util.List;

public abstract class Rule {

    private String ruleName;
    private String errorMessage;

    public Rule() {}

    public abstract boolean validateQuery(List<AbstractClause> query);
    public boolean checkAgregation(String param){
        return param.contains("avg(") || param.contains("sum(") || param.contains("min(") || param.contains("max(") || param.contains("count(");
    }
}
