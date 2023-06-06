package model.converter;

import model.sql_abstraction.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class AggregationConverter extends ParameterConverter{

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

    public List<Document> translate(List<AbstractClause> clauses){

        StringBuilder sb = new StringBuilder();

        for(AbstractClause clause: clauses){
            if(clause instanceof SelectClause){
                documents.add(project(clause));
            }else if(clause instanceof WhereClause){
                documents.add(match(clause));
            }else if(clause instanceof OrderByClause){
                documents.add(sort(clause));
            }else if(clause instanceof GroupByClause){
                documents.add(group(clause));
            }
        }
        sb.append("[{");
        for(Document d: documents)
            sb.append(d.toJson()).append(",");

        String temp = sb.toString();
        result = temp.substring(0, temp.length()-1) + "}]";

        return documents;
    }

    private Document project(AbstractClause clause){

        Aggregation agg = null;
        String project_json;

        for(String param: clause.getParameters()){
            if(aggregation(param)){
                agg = new Aggregation(param);
                aggregations.add(agg);
            }else{
                Parameter parameter = new Parameter(param);
                parameters.add(parameter);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("_id: 0, ");

        for(Aggregation a: aggregations)
            sb.append(a.getAlias()).append(": 1,");

        for(Parameter param: parameters)
            sb.append(param.getParameter()).append(": ").append(param.getProjectParam()).append(",");

        project_json = sb.toString();
        String pom = project_json.substring(0, project_json.length()-1);
        project_json = pom;

        Document project_doc = new Document("$project", project_json);
        System.out.println("za project: "+project_doc.toJson());
        return project_doc;
    }

    private Document sort(AbstractClause clause){

        String sort_json;
        StringBuilder sb = new StringBuilder();
        String pom = "";
        int i = 0;
        ParameterConverter order_by_converter = new SortConverter(clause);
        sort_json = order_by_converter.translate();

        for(String s: clause.getParameters()){
            i++;
            if(aggregation(s)){
                for(Aggregation a: aggregations){
                    if(a.getParameter().equals(s)){
                        pom = a.getAlias();
                    }else{
                        Aggregation agg = new Aggregation(s);
                        aggregations.add(agg);
                        pom = agg.getAlias();
                    }
                }
                sb.append(pom).append(": ").append(getSort(clause.getParameters().get(i+1)));
            }
        }
        pom = sb.toString();
        sort_json = sort_json.concat(pom);
        Document sort_doc = new Document("$sort", sort_json);
        System.out.println("za sort: "+sort_doc.toJson());

        return sort_doc;
    }

    private Document match(AbstractClause clause){

        // u where iskazu nije funkcija agregacije...
        String find_json;
        ParameterConverter where_converter = new FindConverter(clause);
        find_json = where_converter.translate();
        Document match_doc = new Document("$match", find_json);

        System.out.println("za match: "+match_doc.toJson());

        return match_doc;
    }

    private Document group(AbstractClause clause){

        String group_json;
        StringBuilder sb = new StringBuilder();

        if(!parameters.isEmpty())
            sb.append("_id: {");

        for(Parameter p: parameters)
            sb.append(p.getParameter()).append(": ").append(p.getGroupParam()).append(",");

        group_json = sb.toString();
        String pom = group_json.substring(0, group_json.length()-1);
        group_json = pom.concat("},");

        StringBuilder sbb = new StringBuilder();
        for(Aggregation a: aggregations){
            sbb.append(a.getAlias()).append(": {")
                    .append(a.getAggFunction()).append(": ")
                    .append(a.getParameter()).append("},");
        }

        pom = sb.toString();
        String pom2 = pom.substring(0, pom.length()-1);
        group_json = group_json.concat(pom2);

        Document group_doc = new Document("$group", group_json);
        System.out.println("za group: " + group_doc.toJson());
        return null;
    }

    private String getSort(String s){
        if(s.equalsIgnoreCase("asc")) return "1";
        return  "-1";
    }

    @Override
    public String translate() {
        return null;
    }

    /**
    SELECT department_id, manager_id, AVG(salary) FROM employees WHERE department_id > 30
    AND manager_id < 120 GROUP BY department_id, manager_id ORDER BY AVG(salary) ASC

      db.getCollection("hr.employees").aggregate(
     [{
        $match: {
          department_id: { $gt: 30 },
          manager_id: { $lt: 120 }
        }
      },
      {
        $group: {
          _id: {
            department_id: "$department_id",
            manager_id: "$manager_id"
          },
          avgSalary: { $avg: "$salary" }
        }
      },
      {
        $sort: {
          avgSalary: 1
        }
      },
      {
        $project: {
          _id: 0,
          avgSalary: 1,
          department_id: "$_id.department_id",
          manager_id: "$_id.manager_id"
        }
      }]
      */

}
