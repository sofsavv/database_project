package model.sql_abstraction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractClause {

    String keyWord;
    List<String> parameters;

    public AbstractClause(String keyWord){
        this.keyWord = keyWord;
        parameters = new ArrayList<>();
    }

}
