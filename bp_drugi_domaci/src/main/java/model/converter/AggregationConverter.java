package model.converter;

import model.sql_abstraction.AbstractClause;

public class AggregationConverter extends ParameterConverter{

    public AggregationConverter(AbstractClause clause) {
        super(clause);
    }
    @Override
    public String translate() {



        return null;
    }
    /**
    SELECT department_id, manager_id, AVG(salary) FROM employees WHERE department_id > 30
    AND manager_id < 120 GROUP BY department_id, manager_id ORDER BY avgSalary ASC

      db.getCollection("hr.employees").aggregate([
      {
        $matchDocument: {
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
      }
      */

}
