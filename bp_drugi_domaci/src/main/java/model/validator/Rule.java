package model.validator;

import lombok.Getter;
import lombok.Setter;
import model.sql.AbstractClause;

import java.util.List;

@Getter
@Setter
public abstract class Rule {

    private String ruleName;
    private String errorMessage;

    public Rule(String name, String message) {
        this.ruleName = name;
        this.errorMessage = message;
    }

    public abstract boolean validateQuery(List<AbstractClause> query);
    public boolean checkAggregation(String param){
        return param.contains("avg(") || param.contains("sum(") || param.contains("min(") || param.contains("max(") || param.contains("count(");
    }
}
