package model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractClause {

    private String keyWord;
    private List<String> parameters;
    private String temp;

    public AbstractClause(String keyWord){
        this.keyWord = keyWord;
        parameters = new ArrayList<>();
    }

    // treba mi neka lista za svaki clause posebnoo...

}
