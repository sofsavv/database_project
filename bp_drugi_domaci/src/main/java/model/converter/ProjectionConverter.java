package model.converter;

import model.sql_abstraction.AbstractClause;

public class ProjectionConverter extends ParameterConverter{

    public ProjectionConverter(AbstractClause clause) {
        super(clause);
    }
    // db.restaurants.find({}).projection({name:1, cuisine:1, _id:0, stars:1})
    @Override
    public String translate() {

        String projection = "";
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(String param: this.getClause().getParameters()){

            if(param.equals("*")) break;

            sb.append(param);
            if(!param.equals(",")){
                sb.append(": 1");
            }
        }
        sb.append("}");
        projection = sb.toString();

        System.out.println("select: " + projection);

        return projection;
    }
}
