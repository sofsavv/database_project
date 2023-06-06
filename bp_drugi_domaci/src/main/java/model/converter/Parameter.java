package model.converter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameter {

    private String parameter;
    private String groupParam; //department_id: "$department_id"
    private String projectParam; //department_id: "$_id.department_id"

    public Parameter(String parameter){
        String pom = "\"$" + parameter + "\"";
        setGroupParam(pom);
        pom = "\"$_id." + parameter + "\"";
        setProjectParam(pom);
        pom = "\"" + parameter + "\"";
        setParameter(pom);
    }

}
