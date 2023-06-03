package model.validator;

import gui.MainFrame;
import model.parser.QueryParser;
import model.sql_abstraction.AbstractClause;

import javax.swing.*;
import java.util.List;

public class QuerySyntaxRule extends Rule{

    public QuerySyntaxRule() {
        super();
    }

    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        boolean selectFlag = false;
        boolean paramsInBetween = false;


        for(AbstractClause clause: query){

            if(clause.getKeyWord().equalsIgnoreCase("select")){
                selectFlag = true;
                if(clause.getParameters().size() > 0)
                    paramsInBetween = true;
            }


        }
        if(!selectFlag) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Query must contain SELECT clause");
            MainFrame.getInstance().getTextArea().setText("");
            return false;
        }

        if(!paramsInBetween) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Missing params inbetween keywords.");
            MainFrame.getInstance().getTextArea().setText("");

            return false;
        }
        return true;
    }
}
