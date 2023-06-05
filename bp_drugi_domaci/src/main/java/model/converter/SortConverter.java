package model.converter;

import model.sql_abstraction.AbstractClause;

public class SortConverter extends ParameterConverter{

    // ORDER BY column1 ASC, column2 DESC -> {column1: 1, column2: -1}
    public SortConverter(AbstractClause clause) {
        super(clause);
    }

    @Override
    public String translate() {

        String sort = "";
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for(String param: this.getClause().getParameters()){

            if(param.equalsIgnoreCase("asc")){
                sb.append(": 1");
            }else if(param.equalsIgnoreCase("dsc")){
                sb.append(": -1");
            }else sb.append(param);
        }
        sb.append("}");
        sort = sb.toString();
        System.out.println("order by: " + sort);
        return sort;
    }
}
