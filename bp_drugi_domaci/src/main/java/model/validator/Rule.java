package model.validator;

import model.sql_abstraction.AbstractClause;

import java.util.List;

public abstract class Rule {

    private String ruleName;
    private String errorMessage;

    public Rule() {
        this.ruleName = ruleName;
        this.errorMessage = errorMessage;
    }

    public abstract boolean validateQuery(List<AbstractClause> query);

}
