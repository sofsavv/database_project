package model.converter;

import model.sql_abstraction.AbstractClause;

public class ProjectionConverter implements ParameterConverter{

    // db.restaurants.find({}).projection({name:1, cuisine:1, _id:0, stars:1})
    @Override
    public String translate(AbstractClause clause) {

        String projection = "";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(String param: clause.getParameters()){

            if(param.equals(","))
                continue;
            if(param.equals("*")){
                sb.append("}");
                break;
            }
            sb.append(param);
            sb.append(":1, ");
        }
        sb.append("}");
        projection = sb.toString();
        projection = projection.substring(0, projection.lastIndexOf(","));
        System.out.println("param: " + projection);

        return projection;
    }
}
