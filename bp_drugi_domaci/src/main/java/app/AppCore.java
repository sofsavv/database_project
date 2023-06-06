package app;

import database.Database;
import database.MongoDB;
import gui.MainFrame;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppCore {

    private Database database;
    private TableModel tableModel;
    private static MainFrame mainFrame;

    public AppCore() {
        this.database = new MongoDB();
        this.tableModel = new TableModel();
        mainFrame = MainFrame.getInstance();
        mainFrame.setAppCore(this);
        mainFrame.setVisible(true);
    }

    public void readDataFromTable(String fromTable){
        tableModel.setRows(this.database.getDataFromTable(fromTable));
    }

}
