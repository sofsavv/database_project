package model.query;

import lombok.Getter;
import lombok.Setter;
import model.sql.AbstractClause;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SQLquery implements Query {

    private List<AbstractClause> clauses;

    public SQLquery(){
        clauses = new ArrayList<>();
    }

    @Override
    public List<String> getParameters() {

        List<String> sqlQ = new ArrayList<>();

        for(AbstractClause c: clauses){

            sqlQ.add(c.getKeyWord());

               for(String p: c.getParameters()){
                   if(!p.equals(",")){
                       sqlQ.add(p);
                   }
               }
        }
        // reprezentacija sql upita
        return sqlQ;
    }

    public void addClause(AbstractClause clause){
        clauses.add(clause);
    }

}
