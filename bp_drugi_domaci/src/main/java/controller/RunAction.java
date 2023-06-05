package controller;

import gui.MainFrame;
import model.parser.QueryParser;
import model.sql_abstraction.AbstractClause;
import model.validator.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RunAction implements ActionListener {

    private JTextArea textArea;
    List<Rule> rules;
    Rule querySyntaxRule = new QuerySyntaxRule("Syntax rule", "Invalid syntax");
    Rule groupBySelectionRule = new GroupBySelectionRule("Group by rule", "All params from SELECT clause must be under GROUP BY clasue");
    Rule tableJoinRule = new TableJoinRule("Table join rule", "Missing ON or USING clause for joining tables.");
    Rule agregationRule = new AgregationRule("Aggregation rule", "Cannot put agregation function in WHERE clause ");
    public RunAction(JTextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        QueryParser parser = new QueryParser();
        QueryParser parser1 = new QueryParser();
        String query = textArea.getText();
        rules = new ArrayList<>();
        rules.add(querySyntaxRule);
        rules.add(groupBySelectionRule);
        rules.add(tableJoinRule);
        rules.add(agregationRule);

        query = query.toLowerCase();

        List<AbstractClause> subquery = new ArrayList<>();
        List<AbstractClause> clauses = parser.parse(query.replace(","," , ")
                .replace(">="," greater_equal ")
                .replace("!="," not ")
                .replace("<="," less_equal ")
                .replace("="," equals ")
                .replace(">"," greater ")
                .replace("<"," less ")
                .replace("not in"," not_in ")
                .replace("group by", "group_by")
                .replace("order by", "order_by"));

        checkRules(clauses);

        String subQuery = null;
        boolean sq = false;
        for(AbstractClause clause: clauses){
            if(clause.getKeyWord().equalsIgnoreCase("where")){

                for(String whereParams: clause.getParameters()){
//         salary in (select department j)
                    if(whereParams.contains("select")){
                        sq = true;
                        int start = whereParams.indexOf("(");
                        int end = whereParams.lastIndexOf(")");
                        subQuery = whereParams.substring(start+1, end);
                        break;
                    }
                }
            }
        }
        if(sq){
            if(subQuery != null)
                subquery = parser1.parse(subQuery);
            checkRules(subquery);
        }
    }

    private void checkRules(List<AbstractClause> clauses){
        boolean check = false;
        for(Rule rule: rules) {
            if(!rule.validateQuery(clauses)){
                JOptionPane.showMessageDialog(MainFrame.getInstance(), rule.getErrorMessage());
                System.out.println("Greska " + rule.getErrorMessage());
                return;
            }
        }
        //TODO: ako validateQuery vrati zavrsi petlju, znaci da nije doslo do greske
        // i saljemo upit Adapteru.
    }
}
