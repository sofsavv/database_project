package model.packager;

import app.AppCore;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCursor;
import com.sun.tools.javac.Main;
import data.Row;
import gui.MainFrame;
import model.converter.Aggregation;
import model.query.SQLquery;
import model.sql.AbstractClause;
import org.bson.Document;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TablePackager implements Packager{
    List<String> columnNames;
    AbstractClause clause;
    public TablePackager(List<String> columnNames, AbstractClause clause) {
        this.columnNames = columnNames;
        this.clause = clause;
    }

    public void pack(MongoCursor<Document> documents) {

        Row row = null;
        List<Row> rows = new ArrayList<>(120);

//        System.out.println("***********");
//        while (documents.hasNext()){
//            Document d = documents.next();
//            System.out.println("data: " + d.toJson());
//        }

        while (documents.hasNext()){
            row = new Row();
            Document document = documents.next();
            for(String data: columnNames) {
//                System.out.println("data: " + data);
                if (data.contains(","))
                    continue;
                try {
//                    row.addField("document_id", document.get("avgSalary"));
                    row.addField(data, document.get(data));
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    System.out.println("null pointer");
                }
                rows.add(row);
            }
        }
//        int i = 0;
//        for(Row row1: rows){
//            System.out.println(i + ". " + row1);
//            i++;
//        }
        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
    }

    public void packAggregation(MongoCursor<Document> documents){

        List<String> id_params = new ArrayList<>();
        List<String> fs_agg = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        Aggregation a = null;
        Row row = null;

//        System.out.println("***********");
//        while (documents.hasNext()){
//            Document d = documents.next();
//            System.out.println("data: " + d.toJson());
//        }

        for(String param: clause.getParameters()){
            if(param.contains("(") && param.contains(")")){
                a = new Aggregation(param);
                fs_agg.add(a.getAlias());
            }else id_params.add(param);
        }

        while (documents.hasNext()) {
            Document d1 = documents.next();
            row = new Row();

            Document d2 = null;
            try {
                d2 = d1.get("_id", Document.class);
            }catch (ClassCastException e){
                e.printStackTrace();
            }

            if(d2 != null) {
                for (String id : id_params) {
                    row.addField(id, d2.get(id));
                }
            }

            for (String agg: fs_agg){
                row.addField(agg, d1.get(a.getAlias()));
                rows.add(row);
            }
        }
        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
    }

}
