package model.converter;

import model.sql.AbstractClause;

public class CollectionConverter extends ParameterConverter{

    public CollectionConverter(AbstractClause clause) {
        super(clause);
    }

    @Override
    public String translate() {
        String collection = this.getClause().getParameters().get(0);
        System.out.println("from: " + collection);
        return collection;
    }
}
