package model.parser;

import model.sql_abstraction.AbstractClause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryParser {

    StateManager stateManager;
    List<AbstractClause> clauses;

    public QueryParser(){
        clauses = new ArrayList<>();
    }

    public void parse(String query){

        System.out.println("query: " + query);
        List<String> tokens = new ArrayList<>();
        tokens = List.of(query.split(" "));

        if(!tokens.get(0).equalsIgnoreCase("select")){
            // nije pocelo sa SELECT treba neka poruka
            // TODO validator
        }else{
            stateManager = new StateManager(); // pocinje sa select state
        }

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
