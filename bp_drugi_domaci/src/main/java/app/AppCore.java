package app;

import database.Database;
import database.MongoDB;
import database.settings.Settings;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppCore {

    private Database database;
    private Settings settings;
    private TableModel tableModel;

    public AppCore() {
        this.database = new MongoDB();
        this.tableModel = new TableModel();
    }

    public void readDataFromTable(String fromTable){
        tableModel.setRows(this.database.getDataFromTable(fromTable));
    }

}
