package model.validator;

import gui.MainFrame;
import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.GroupByClause;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GroupBySelectionRule extends Rule{
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

            for(String param: selectParams){
                if(!checkAgregation(param)){
                    if(!groupByParams.contains(param)){
                        JOptionPane.showMessageDialog(MainFrame.getInstance(), "Invalid GROUP BY syntax.");
                        return false;
                    }
                }
            }


        return true;
    }

    public boolean checkAgregation(String param){
        return param.contains("avg(") || param.contains("sum(") || param.contains("min(") || param.contains("max(") || param.contains("count(");
    }

}
