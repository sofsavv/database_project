package model.converter;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Aggregation {

    private String alias;
    private String aggFunction;
    private String parameter;

    public Aggregation(String aggregation){
        setAttributes(aggregation);
    }

    private void setAttributes(String aggregation){
        String agg = "\"" + "$" + aggregation.substring(0, 3) + "\"";
        setAggFunction(agg);

        String par = aggregation.substring(4, aggregation.length()-1).substring(0, 1).toUpperCase();
        String rest = aggregation.substring(4, aggregation.length()-1).substring(1);
        agg = aggregation.substring(0, 3);
        String a = agg + par + rest;
        setAlias(a);
        a = "\"" + "$" + aggregation.substring(4, aggregation.length()-1) + "\"";
        setParameter(a);
    }

}
