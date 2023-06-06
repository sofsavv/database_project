package model.validator;

import model.sql.AbstractClause;
import java.util.ArrayList;
import java.util.List;

public class GroupBySelectionRule extends Rule{
    public GroupBySelectionRule(String name, String message) {
        super(name, message);
    }

    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        //sve sto je uz select a nije uz avg,sum,min,max mora biti i uz group by
        boolean containsGroupByClause = false;
        for(AbstractClause clause: query){
            if(clause.getKeyWord().equalsIgnoreCase("group by"))
                containsGroupByClause = true;
        }

        List<String> selectParams = new ArrayList<>();
        List<String> groupByParams = new ArrayList<>();
        groupByParams.add(",");

            if(containsGroupByClause){
                for(AbstractClause clause: query){
                    switch (clause.getKeyWord()){
                        case "select": selectParams.addAll(clause.getParameters()); break;
                        case "group by": groupByParams.addAll(clause.getParameters()); break;
                    }
                }
            }else{
                return true;
            }
        System.out.println("Group by parametri " + groupByParams);
            boolean valid = false;
        //select name, salary, avg(age) from gr group by name
        //select name ,  s ,  avg(s) from hr group_by name ,
            for(String param: selectParams){
                if(!checkAgregation(param)){
                    for(String groupParams: groupByParams){
                        if(groupParams.equalsIgnoreCase(param)){
                            valid = true;
                        }
                    }
                    if(!valid){
                        return false;
                    }else
                        valid = false;
                }
            }


        return true;
    }



}
