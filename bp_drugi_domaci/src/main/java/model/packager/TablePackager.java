package model.packager;

import app.AppCore;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCursor;
import com.sun.tools.javac.Main;
import data.Row;
import gui.MainFrame;
import org.bson.Document;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TablePackager implements Packager{
    List<String> columnNames;
    public TablePackager(List<String> columnNames) {
    this.columnNames = columnNames;
    }

    public void pack(MongoCursor<Document> documents) {

        Row row = null;
        List<Row> rows = new ArrayList<>(120);

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
        int i = 0;
        for(Row row1: rows){
            System.out.println(i + ". " + row1);
            i++;
        }

        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
        MainFrame.getInstance().getJTable().setModel(MainFrame.getInstance().getAppCore().getTableModel());

    }
}
