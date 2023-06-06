package model.packager;

import com.mongodb.client.MongoCursor;
import data.Row;
import gui.MainFrame;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;

public class TablePackager implements Packager{
    List<String> selectParams;
    public TablePackager(List<String> selectParams) {
    this.selectParams = selectParams;
    }

    public void pack(MongoCursor<org.bson.Document>  documents) {

        Row row;
        List<Row> rows = new ArrayList<>();
        while (documents.hasNext()){

            Document curr = (Document) documents.next();
            row = new Row();

            for(String param: selectParams){
                row.addField(param, curr.getProperty(param));
            }
            rows.add(row);
        }

        MainFrame.getInstance().getAppCore().getTableModel().setRows(rows);
    }
}
