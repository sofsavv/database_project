package database;


import data.Row;

import java.util.List;

public interface Database {


    List<Row> getDataFromTable(String from);
}
