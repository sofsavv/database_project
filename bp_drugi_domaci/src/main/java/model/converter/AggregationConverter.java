package model.converter;

import model.sql.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class AggregationConverter extends ParameterConverter {

    List<Document> documents;
    List<Aggregation> aggregations;
    List<Parameter> parameters;
    String result;


    public AggregationConverter(AbstractClause clause) {
        super(clause);
        documents = new ArrayList<>();
        aggregations = new ArrayList<>();
        parameters = new ArrayList<>();
    }

    public List<Document> translate(List<AbstractClause> clauses) {

        StringBuilder sb = new StringBuilder();

        for (AbstractClause clause : clauses) {
            if (clause instanceof SelectClause) {
                Document doc = project(clause);
//                documents.add(doc);
            } else if (clause instanceof WhereClause) {
                documents.add(match(clause));
            } else if (clause instanceof OrderByClause) {
                documents.add(sort(clause));
            } else if (clause instanceof GroupByClause) {
                documents.add(group(clause));
            }
        }
        sb.append("[");
        for (Document d : documents) {
            sb.append(d.toJson()).append(",");
        }

        String temp = sb.toString();
        result = temp.substring(0, temp.length() - 1) + "]";

        System.out.println("RESULT: " + result);

        return documents;
    }

    private Document project(AbstractClause clause) {

        Aggregation agg = null;
        String project_json;

        for (String param : clause.getParameters()) {
            if (aggregation(param)) {
                agg = new Aggregation(param);
                aggregations.add(agg);
            } else if (!param.equals(",")) {
                Parameter parameter = new Parameter(param);
                parameters.add(parameter);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("_id: 0, ");

        for (Aggregation a : aggregations)
            sb.append(a.getAlias()).append(": 1,");

        for (Parameter param : parameters)
            sb.append(param.getParameter()).append(": ").append(param.getProjectParam()).append(",");


        project_json = sb.toString();
        String pom = project_json.substring(0, project_json.length() - 1);
        project_json = pom;
        System.out.println("project JSON: " + project_json);

        return new Document("$project", project_json);
    }

    private Document sort(AbstractClause clause) {

        String sort_json;

        ParameterConverter order_by_converter = new SortConverter(clause);
        sort_json = order_by_converter.translate();

        return new Document("$sort", sort_json);
    }

    private Document match(AbstractClause clause) {

        // u where iskazu nije funkcija agregacije...
        // where department_id  $gt  30 or manager_id  $lt  120

        String find_json;
        ParameterConverter where_converter = new FindConverter(clause);
        find_json = where_converter.translate();
        Document doc = Document.parse(find_json);

        return new Document("$match", doc);
    }

    private Document group(AbstractClause clause) {

        String group_str;
        StringBuilder sb = new StringBuilder();

        if (!parameters.isEmpty())
            sb.append("_id: {");

        for (Parameter p : parameters)
            sb.append(p.getParameter()).append(": ").append(p.getGroupParam()).append(",");

        group_str = sb.toString();
        String pom = group_str.substring(0, group_str.length() - 1);
        group_str = pom.concat("},");

        sb = new StringBuilder();
        for (Aggregation a : aggregations) {
            sb.append(a.getAlias()).append(": {")
                    .append(a.getAggFunction()).append(": ")
                    .append(a.getParameter()).append("},");
        }

        pom = sb.toString();
        String pom2 = pom.substring(0, pom.length() - 1);
        group_str = group_str.concat(pom2);
        String group_json = "{" + group_str + "}";
        Document doc = Document.parse(group_json);

        return new Document("$group", doc);
    }

    @Override
    public String translate() {
        return null;
    }


}
