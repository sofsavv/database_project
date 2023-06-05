package model.validator;


import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.WhereClause;

import java.util.List;


public class AgregationRule extends Rule{
    public AgregationRule(String name, String message) {
        super(name, message);
    }

    //ako imamo where klauzu, nakon nje ne sme ici funkcija agregacije
    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        boolean whereClause = false;

        AbstractClause clause = null;
        for(AbstractClause clauseCheck: query){
            if(clauseCheck.getKeyWord().equalsIgnoreCase("where")){
                whereClause = true;
                 clause = new WhereClause();
                 clause.getParameters().addAll(clauseCheck.getParameters());
            }
        }

        if(!whereClause)
            return true;

        List<String> params = clause.getParameters();
        //prolazimo kroz listu i traizmo funkc agrgr
        for(String param: params){
            if(checkAgregation(param)){
                return false;
            }
        }

        return true;
    }
}
