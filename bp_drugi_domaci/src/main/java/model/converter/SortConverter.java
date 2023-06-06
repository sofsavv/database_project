package model.converter;

import model.sql.AbstractClause;

public class SortConverter extends ParameterConverter{

    // ORDER BY column1 ASC, column2 DESC -> {column1: 1, column2: -1}
    public SortConverter(AbstractClause clause) {
        super(clause);
    }

    @Override
    public String translate() {

        String sort = "";
        String alias;
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for(String param: this.getClause().getParameters()){

            if(aggregation(param)) {
                Aggregation agg = new Aggregation(param);
                alias = agg.getAlias();
                sb.append(alias);
                continue;
            }
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
