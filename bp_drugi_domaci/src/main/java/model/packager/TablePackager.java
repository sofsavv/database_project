package model.packager;


import com.mongodb.client.MongoCursor;
import data.Row;
import gui.MainFrame;
import model.converter.Aggregation;
import model.sql.AbstractClause;
import org.bson.Document;
import java.util.*;
import java.util.List;

public class TablePackager implements Packager{
    List<String> columnNames;
    AbstractClause clause;
    public TablePackager(List<String> columnNames, AbstractClause clause) {
        this.columnNames = columnNames;
        this.clause = clause;
    }

    public void pack(MongoCursor<Document> documents) {

        Row row;
        List<Row> rows = new ArrayList<>();


        while (documents.hasNext()){
            row = new Row();
            Document document = documents.next();
            for(String data: columnNames) {
                //last_name - king
                if (data.contains(","))
                    continue;
                try {
                    row.addField(data, document.get(data));
                } catch (NoSuchElementException e) {
                    e.getMessage();
                    System.out.println("null pointer");
                }

            }
            rows.add(row);
        }

        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
    }

    public void packAggregation(MongoCursor<Document> documents){

        List<String> id_params = new ArrayList<>();
        List<String> fs_agg = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        Aggregation a = null;
        Row row;

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

            for(Row r: rows){
                System.out.println(r.getFields());
            }

            rows.removeIf(r -> r.getFields().isEmpty());
        }

        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
//        MainFrame.getInstance().getJTable().setModel(MainFrame.getInstance().getAppCore().getTableModel());

    }
}
