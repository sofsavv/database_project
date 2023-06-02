package model.parser;

public class StateManager {

    private ParserState current;
    private FromState fromState;
    private GroupByState groupByState;
    private JoinState joinState;
    private OrderByState orderByState;
    private SelectState selectState;
    private WhereState whereState;

    public StateManager(){
        fromState = new FromState();
        groupByState = new GroupByState();
        joinState = new JoinState();
        orderByState = new OrderByState();
        selectState = new SelectState();
        whereState = new WhereState();
        current = selectState;
    }

    public ParserState getCurrentState(){
        return current;
    }
    public void setFromState(){
        current = fromState;
    }
    public void setGroupByState(){
        current = groupByState;
    }
    public void setJoinState(){ current = joinState;}
    public void setOrderByState() { current = orderByState; }
    public void setWhereState() { current = whereState; }
    public void setSelectState(){
        current = selectState;
    }

}
