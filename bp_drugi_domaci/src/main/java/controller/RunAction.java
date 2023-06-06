package controller;


import com.mongodb.client.MongoCursor;
import model.converter.Mapper;

import gui.MainFrame;

import model.executor.Executor;
import model.packager.Packager;
import model.packager.TablePackager;
import model.parser.QueryParser;
import model.sql.AbstractClause;
import model.validator.*;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        // za adapter replace
        List<AbstractClause> subquery = new ArrayList<>();
        List<AbstractClause> clauses = parser.parse(query.replace(","," , ")
                .replace(">="," $gte ")
                .replace("!="," $ne ")
                .replace("<="," $lte ")
                .replace("="," $eq ")
                .replace(">"," $gt ")
                .replace("<"," $lt ")
                .replace("not in"," not_in ")
                .replace("group by", "group_by")
                .replace("order by", "order_by"));

        List<String> selectParams = new ArrayList<>();
        Optional<AbstractClause> match = clauses.stream().
                filter(c -> c.getKeyWord().equalsIgnoreCase("select")).
                findFirst();

        checkRules(clauses);

        StringBuilder stringBuilder = new StringBuilder();
        boolean sq = false;
        for(AbstractClause clause: clauses){
            if(clause.getKeyWord().equalsIgnoreCase("where")){
                for(String whereParams: clause.getParameters()){
                    if(whereParams.contains("select")){
                        sq = true;
                        int start = whereParams.indexOf("(");
                        int end = whereParams.lastIndexOf(")");
                        stringBuilder.append(whereParams.substring(start+1, end));
                        break;
                    }
                }
            }
        }
        if(sq){
            subquery = parser1.parse(stringBuilder.toString());
            checkRules(subquery);
        }

        if(match.isPresent())
            selectParams = match.get().getParameters();

        Mapper mapper = new Mapper(parser.getClauses());
        Executor executor = new Executor();
        HashMap<String, String> strings = mapper.map();
        MongoCursor<Document> docs = executor.execute(strings);
        Packager packager = new TablePackager(selectParams);
        packager.pack(docs);
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
