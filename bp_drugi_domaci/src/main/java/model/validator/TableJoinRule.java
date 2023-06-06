package model.validator;

import model.sql.AbstractClause;

import java.util.ArrayList;
import java.util.List;

public class TableJoinRule extends Rule{
    public TableJoinRule(String name, String message) {
        super(name, message);
    }

    @Override
    public boolean validateQuery(List<AbstractClause> query) {

        // posle join-a ide using ili on
        List<String> params = new ArrayList<>();

        boolean joinClause = false;
        boolean contains = false;

        for(AbstractClause clause: query)
            if(clause.getKeyWord().equalsIgnoreCase("join")){
                joinClause = true;
                params.addAll(clause.getParameters());
                break;
            }

        if(joinClause) {
            for(String param: params){
                if(param.equalsIgnoreCase("ON") || param.equalsIgnoreCase("USING")){
                    contains = true;
                    break;
                }
            }
            return contains;

        }


        return true;




    }
}
