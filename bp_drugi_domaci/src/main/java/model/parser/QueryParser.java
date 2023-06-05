package model.parser;

import lombok.Getter;
import lombok.Setter;
import model.sql_abstraction.AbstractClause;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Getter
@Setter
public class QueryParser {

    StateManager stateManager;
    List<AbstractClause> clauses;

    boolean subquery = false;

    public QueryParser(){
        clauses = new ArrayList<>();
    }

    public List<AbstractClause> parse(String query){

        System.out.println("query: " + query);
        List<String> tokens = new ArrayList<>();
        tokens = List.of(query.split(" "));

        stateManager = new StateManager();
        Iterator<String> it = tokens.iterator();
        String tok;

        while (it.hasNext()){

            tok = it.next();

            if(tok.equals(""))
                continue;

            if(tok.endsWith(")") && !isAggregation(tok) && subquery){
                subquery = false;
                stateManager.getCurrentState().process(tok, false, true);

            }else if(tok.startsWith("(")  || subquery){
                subquery = true;
                stateManager.getCurrentState().process(tok, false, true);

            } else if(!isClause(tok)) {
                stateManager.getCurrentState().process(tok, false, false);
//                stateManager.getCurrentState().
                //TODO setujemo
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
        return clauses;
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
