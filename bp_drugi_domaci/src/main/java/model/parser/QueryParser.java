package model.parser;

import lombok.Getter;
import lombok.Setter;
import model.sql_abstraction.AbstractClause;
import model.validator.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Getter
@Setter
public class QueryParser {

    StateManager stateManager;
    List<AbstractClause> clauses;
    List<Rule> rules;
    Rule querySyntaxRule = new QuerySyntaxRule();
    Rule groupBySelectionRule = new GroupBySelectionRule();
    Rule tableJoinRule = new TableJoinRule();
    Rule agregationRule = new AgregationRule();

    public QueryParser(){
        clauses = new ArrayList<>();
    }

    public void parse(String query){

        System.out.println("query: " + query);
        List<String> tokens = new ArrayList<>();
        tokens = List.of(query.split(" "));

        rules = new ArrayList<>();
        rules.add(querySyntaxRule);
        rules.add(groupBySelectionRule);
        rules.add(tableJoinRule);
        rules.add(agregationRule);

        stateManager = new StateManager(); // pocinje sa select state

        Iterator<String> it = tokens.iterator();
        String tok;

        while (it.hasNext()){

            tok = it.next();

            if(tok.equals(""))
                continue;

            if(tok.equalsIgnoreCase("order") && it.hasNext()){

                if(it.next().equalsIgnoreCase("by")){
                    System.out.println("in order by state...");
                    tok = it.next();
                    stateManager.setOrderByState();
                    stateManager.getCurrentState().process(tok, true);
                }
            }else if(tok.equalsIgnoreCase("group") && it.hasNext()){
                if(it.next().equalsIgnoreCase("by")){
                    System.out.println("in group by state...");

                    tok = it.next();
                    stateManager.setGroupByState();
                    AbstractClause clause = stateManager.getCurrentState().process(tok, true);
                    clauses.add(clause);

                    stateManager.getCurrentState().process(tok, true);
                }
            }

            if(!isClause(tok)) {
                stateManager.getCurrentState().process(tok, false);
            } else {
                AbstractClause clause = stateManager.getCurrentState().process(tok, true);
                if(clause != null){
                    clauses.add(clause);
                }
            }

        }
        checkRules(clauses);
    }


    private void checkRules(List<AbstractClause> clauses){
        boolean check = false;
        for(Rule rule: rules) {
            if(!rule.validateQuery(clauses)){
            //TODO: prekidanje programa tj ne saljemo upit dalje nego opet cekamo input.
                System.out.println("Greskaaa kraj. ");
                return;
            }
        }
        //TODO: ako validateQuery vrati zavrsi petlju, znaci da nije doslo do greske
        // i saljemo upit Adapteru.
    }

    private boolean isClause(String token){

        if(token.equalsIgnoreCase("select")){
            stateManager.getCurrentState().process(token, true);
            stateManager.setSelectState();
            System.out.println("start select state...");
            return true;
        }else if(token.equalsIgnoreCase("from")){
            stateManager.getCurrentState().process(token, true);
            stateManager.setFromState();
            System.out.println("start from state...");
            return true;
        }else if(token.equalsIgnoreCase("where")){
            stateManager.getCurrentState().process(token, true);
            stateManager.setWhereState();
            System.out.println("start where state...");
            return true;
        }else if(token.equalsIgnoreCase("join")){
            stateManager.getCurrentState().process(token, true);
            stateManager.setJoinState();
            System.out.println("start join state...");
            return true;
        }
        return false;
    }

}
