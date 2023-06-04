package model.parser;

import lombok.Getter;
import lombok.Setter;
import model.sql_abstraction.AbstractClause;
import model.validator.*;

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
    boolean subquery = false;

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

        stateManager = new StateManager();

        Iterator<String> it = tokens.iterator();
        String tok;

        while (it.hasNext()){

            tok = it.next();
            if(tok.equals(""))
                continue;

            // subquery
            if(tok.endsWith(")") && !isAggregation(tok) && subquery){
                subquery = false;
                stateManager.getCurrentState().process(tok, false, true);
                System.out.println("token sa ) " + tok);

            }else if(tok.startsWith("(")  || subquery){
                subquery = true;
                stateManager.getCurrentState().process(tok, false, true);

            } else if(!isClause(tok)) {
                stateManager.getCurrentState().process(tok, false, false);
            } else {
                AbstractClause clause = stateManager.getCurrentState().process(tok, true, false);
                if(clause != null){
                    clauses.add(clause);
                }
            }
        }
        System.out.println("izlistane klauze");
        for(AbstractClause c: clauses){
            System.out.println(c.getKeyWord());
            for (String s: c.getParameters()){
                System.out.println("param: " + s);
            }
        }

//        checkRules(clauses);
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
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setSelectState();
            System.out.println("start select state...");
            return true;
        }else if(token.equalsIgnoreCase("from")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setFromState();
            System.out.println("start from state...");
            return true;
        }else if(token.equalsIgnoreCase("where")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setWhereState();
            System.out.println("start where state...");
            return true;
        }else if(token.equalsIgnoreCase("join")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setJoinState();
            System.out.println("start join state...");
            return true;
        }else if(token.equalsIgnoreCase("group_by")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setGroupByState();
            System.out.println("start group_by state..");
            return true;
        }else if(token.equalsIgnoreCase("order_by")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setOrderByState();
            System.out.println("start order_by state..");
            return true;
        }
        return false;
    }

    private boolean isAggregation(String param){
        return (param.contains("avg(") && param.endsWith(")"))
                || (param.contains("sum(") && param.endsWith(")"))
                || (param.contains("min(") && param.endsWith(")"))
                || (param.contains("max(") && param.endsWith(")"))
                || (param.contains("count(") && param.endsWith(")"));
    }

}
