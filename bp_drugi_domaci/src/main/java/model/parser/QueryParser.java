package model.parser;

import model.sql_abstraction.AbstractClause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryParser {

    StateManager stateManager;
//    List<AbstractClause> clauses;

    public void parse(String query){

        List<String> tokens = new ArrayList<>();
        tokens = List.of(query.split(" "));

        if(!tokens.get(0).equalsIgnoreCase("select")){
            // nije pocelo sa SELECT treba neka poruka
        }else{
            stateManager = new StateManager(); // pocinje sa select state
        }

        Iterator<String> it = tokens.iterator();
        String tok;

        while (it.hasNext()){

            // edge case
            tok = it.next();
            if(tok.equalsIgnoreCase("order") && it.hasNext()){

                if(it.next().equalsIgnoreCase("by")){
                    tok = it.next();
                    stateManager.setOrderByState();
                    stateManager.getCurrentState().process(tok);
                }else{
                    // moze da bude neka kolona order a moze i greska???
                }
            }else if(tok.equalsIgnoreCase("group") && it.hasNext()){

                if(it.next().equalsIgnoreCase("by")){
                    tok = it.next();
                    stateManager.setGroupByState();
                    stateManager.getCurrentState().process(tok);
                }else{
                    // ??????
                }
            }
            if(!isClause(tok))
                stateManager.getCurrentState().process(tok);
            else stateManager.getCurrentState().process(tok);
        }
    }

    private boolean isClause(String token){

        if(token.equalsIgnoreCase("select")){
            stateManager.setSelectState();
            return true;
        }else if(token.equalsIgnoreCase("from")){
            stateManager.setFromState();
            return true;
        }else if(token.equalsIgnoreCase("where")){
            stateManager.setWhereState();
            return true;
        }else if(token.equalsIgnoreCase("join")){
            stateManager.setJoinState();
            return true;
        }
        return false;
    }

}
