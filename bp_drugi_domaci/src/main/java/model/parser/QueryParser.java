package model.parser;

import lombok.Getter;
import lombok.Setter;
import model.sql.AbstractClause;
import model.query.SQLquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Getter
@Setter
public class QueryParser {

    StateManager stateManager;
    SQLquery sqlQuery;
    boolean subquery = false;

    public QueryParser(){
        sqlQuery = new SQLquery();
    }

    public void parse(String query){

        System.out.println("query: " + query);
        List<String> tokens = new ArrayList<>();
        tokens = List.of(query.split(" "));

        stateManager = new StateManager();
        Iterator<String> it = tokens.iterator();
        String tok;

        while (it.hasNext()){

            tok = it.next();
            if(tok.equals("")) continue;

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
                    sqlQuery.addClause(clause);
                }
            }
        }
        System.out.println("NOVO: ");
        for (String s: sqlQuery.getParameters()){
            System.out.println(s);
        }
        System.out.println("NOVO");

        // sluzi samo da se spakuje u sql query lista clausa...
    }

    private boolean isClause(String token){

        if(token.equalsIgnoreCase("select")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setSelectState();
            return true;
        }else if(token.equalsIgnoreCase("from")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setFromState();
            return true;
        }else if(token.equalsIgnoreCase("where")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setWhereState();
            return true;
        }else if(token.equalsIgnoreCase("join")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setJoinState();
            return true;
        }else if(token.equalsIgnoreCase("group_by")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setGroupByState();
            return true;
        }else if(token.equalsIgnoreCase("order_by")){
            stateManager.getCurrentState().process(token, true, false);
            stateManager.setOrderByState();
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
