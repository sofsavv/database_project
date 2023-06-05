package model.validator;

import gui.MainFrame;
import model.parser.QueryParser;
import model.sql_abstraction.AbstractClause;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class QuerySyntaxRule extends Rule{

    public QuerySyntaxRule() {
        super();
    }

    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        boolean selectFlag = false;
        boolean fromFlag = false;
        boolean groupByFlag = false;

        List<String> selectParams = new ArrayList<>();
        for(AbstractClause clause: query){

            switch (clause.getKeyWord()){
                case "select":selectFlag =  true; selectParams.addAll(clause.getParameters());break;
                case "from":fromFlag = true; break;
                case "group by": groupByFlag = true; break;
            }

            if(clause.getParameters().size() == 0)
                   JOptionPane.showMessageDialog(MainFrame.getInstance(), "Missing params inbetween keywords.");
        }

        if(!selectFlag) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Query must contain SELECT clause.");
        }
        if(!fromFlag){
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Query must contain FROM clause.");
        }

        boolean isAgg = false;
        if(groupByFlag){
            System.out.println("parametri " + selectParams);
            for(String param: selectParams){
                if(checkAgregation(param)){
                    isAgg = true;
                    break;
                }
            }
            if(!isAgg) {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), "No agregation function for GROUP BY clause");
                return false;
            }else {
                return true;
            }

        }else
            return true;

    }
}


