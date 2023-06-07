package controller;

import adapter.Adapter;
import adapter.MongoDBAdapter;
import com.mongodb.client.MongoCursor;
import model.converter.Mapper;

import gui.MainFrame;

import model.executor.Executor;
import model.packager.Packager;
import model.packager.TablePackager;
import model.parser.QueryParser;
import model.sql.AbstractClause;
import model.sql.SelectClause;
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
    private boolean agg;
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
        QueryParser subParser = new QueryParser();
        String query = textArea.getText();
        AbstractClause selectClause = null;

        rules = new ArrayList<>();
        rules.add(querySyntaxRule);
        rules.add(groupBySelectionRule);
        rules.add(tableJoinRule);
        rules.add(agregationRule);

        query = query.toLowerCase();
        query = query.replace(",", " , ")
                .replace("order by", "order_by")
                .replace("group by", "group_by")
                .replace("not in", "not_in")
                .replace(">=", " $gte ")
                .replace("<=", " $lte ")
                .replace(">", " $gt ")
                .replace("<", " $lt ")
                .replace("!=", " $ne ")
                .replace("=", " $eq ")
                .replace("'%","/")
                .replace("%'","/");

        parser.parse(query);
        Adapter mongoDBAdapter = new MongoDBAdapter(parser.getSqlQuery());

        for(AbstractClause clause: parser.getSqlQuery().getClauses()){
            if(clause instanceof SelectClause) selectClause = clause;
        }

        checkRules(parser.getSqlQuery().getClauses());

        List<String> selectParams = new ArrayList<>();
        Optional<AbstractClause> match = parser.getSqlQuery().getClauses().stream().
                filter(c -> c.getKeyWord().equalsIgnoreCase("select")).
                findFirst();

        if(match.isPresent())
            selectParams = match.get().getParameters();

        List<AbstractClause> subquery = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        boolean sq = false;
        for(AbstractClause clause: parser.getSqlQuery().getClauses()){
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
            subParser.parse(stringBuilder.toString());
            checkRules(subParser.getSqlQuery().getClauses());
        }

        Mapper mapper = new Mapper(parser.getSqlQuery());
        Executor executor = new Executor();
        MongoCursor<Document> docs;
        Packager packager = new TablePackager(selectParams, selectClause);

        for(String s: selectParams){
            System.out.println("select par: " + s);
            if(aggregation(s)) {
                agg = true;
                System.out.println("usao");
            }
        }

        if(!agg) {
            HashMap<String, String> strings = mapper.map();
            docs = executor.execute(strings);
            packager.pack(docs);
        } else {
            docs = executor.executeAggregation(parser.getSqlQuery());
            packager.packAggregation(docs);
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
    public boolean aggregation(String str){

        return (str.startsWith("avg(") && str.endsWith(")"))
                || (str.startsWith("sum(") && str.endsWith(")"))
                || (str.startsWith("count(") && str.endsWith(")"))
                || (str.startsWith("min(") && str.endsWith(")"))
                || (str.startsWith("max(") && str.endsWith(")"));

    }

}
