package model.parser;

import model.sql_abstraction.AbstractClause;
import model.sql_abstraction.WhereClause;

public class WhereState extends ParserState{
    AbstractClause where = null;
    StringBuilder sb = new StringBuilder();
    public AbstractClause process(String token, boolean next, boolean bracket) {

        if(where == null)
            where = new WhereClause();

//select department_name from hr.departments where manager_id in
//    (select employee_id from hr.employees where last_name like 'h%')
        if(bracket){

            if(token.contains(")") && !isAggregation(token)) {

                sb.append(token);
                where.setTemp(sb.toString());
                where.getParameters().add(where.getTemp());

                System.out.println("temp na kraju: " + where.getTemp());

                where.setTemp("");
                bracket = false;
                sb.deleteCharAt(0);
                sb.deleteCharAt(sb.length() - 1);
            }
            else{
                sb.append(token);
                sb.append(" ");
            }

        }else if(!token.equalsIgnoreCase("where") && !next && !token.matches("\\s+")){
            where.getParameters().add(token);
//            System.out.println("param: " + token);
        }

        return returnClause(next, where);
    }

}
