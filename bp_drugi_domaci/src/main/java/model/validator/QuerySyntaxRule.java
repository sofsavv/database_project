package model.validator;

import model.sql_abstraction.AbstractClause;
import java.util.ArrayList;
import java.util.List;

public class QuerySyntaxRule extends Rule{


    public QuerySyntaxRule(String name, String message) {
        super(name, message);
    }

    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        boolean selectFlag = false;
        boolean fromFlag = false;
        boolean groupByFlag = false;

        List<String> selectParams = new ArrayList<>();
        for(AbstractClause clause: query){

            if(clause.getKeyWord().equalsIgnoreCase("select")){
                selectFlag =  true;
                selectParams.addAll(clause.getParameters());

            }else if(clause.getKeyWord().equalsIgnoreCase("from")){
                fromFlag = true;
                if(clause.getParameters().size() != 1)
                    return false;
            }else if(clause.getKeyWord().equalsIgnoreCase("group by")){
                groupByFlag = true;

            }

            if(clause.getParameters().size() == 0)
                   return false;
        }

        if(!fromFlag || !selectFlag)
            return false;



        boolean isAgg = false;

        if(groupByFlag){
            System.out.println("parametri " + selectParams);
            for(String param: selectParams){
                if(checkAgregation(param)){
                    isAgg = true;
                    break;
                }
            }
            return isAgg;

        }else
            return true;

    }
}


