package model.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhereClause extends AbstractClause{
    public WhereClause() {
        super("where");
    }

}
