package model.converter;

import model.sql_abstraction.AbstractClause;

public class FindConverter extends ParameterConverter{

    // where a > 10 and trek = 'sofija' or b = 10
    // {$or: [{$and: [{a: {$gt: 10 }}, {trek: 'sofija'}] }, {b: 10} ]}

    public FindConverter(AbstractClause clause) {
        super(clause);
    }

    @Override
    public String translate() {

        boolean operator = false;
        boolean close = false;
        int bracket = 0;
        String find;
        StringBuilder sb = new StringBuilder();

        for(String param: this.getClause().getParameters()){

            if(param.equalsIgnoreCase("and")){

                if(close) {
                    sb.append("]").append("}");
                    bracket--;
                }
                sb.append(",");
                sb.insert(0, "{$and: [");
                close = true;
                bracket++;

            }else if(param.equalsIgnoreCase("or")){

                if(close) {
                    sb.append("]").append("}");
                    bracket--;
                }
                sb.append(",");
                sb.insert(0, "{$or: [");
                close = true;
                bracket++;

            }else if(operator){
                sb.append(param).append("}}");
                operator = false;
            }else {
                sb.append("{").append(param).append(": ");
                operator = isOperator(param);
            }
        }

        bracket *= 2;

        for(int k = 0; k < bracket; k++){
            if(k%2 == 0){
                sb.append("]");
            }else sb.append("}");
        }

        find = sb.toString();
        System.out.println("where: " + find);
//        find = find.substring(0, find.lastIndexOf(","));
        return find;
    }

    private boolean isOperator(String str){
        return str.equals("$gt") || str.equals("$gte")
                || str.equals("$lt") || str.equals("$lte")
                || str.equals("$ne") || str.equals("$eq");
    }
}
