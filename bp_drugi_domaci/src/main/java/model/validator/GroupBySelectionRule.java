package model.validator;

import model.sql_abstraction.AbstractClause;

import java.util.List;

public class GroupBySelectionRule extends Rule{
    @Override
    public boolean validateQuery(List<AbstractClause> query) {
        return false;
    }
}
