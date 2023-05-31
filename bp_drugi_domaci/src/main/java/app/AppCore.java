package app;

import database.Database;
import database.MYSQLdatabase;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;
import utils.Constants;

import javax.swing.tree.DefaultTreeModel;

@Getter
@Setter
public class AppCore {

    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private DefaultTreeModel defaultTreeModel;


    public AppCore() {
        this.settings = initSettings();
        this.database =  new MYSQLdatabase(this.settings);
        this.tableModel = new TableModel();


    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mysql_ip", Constants.MYSQL_IP);
        settingsImplementation.addParameter("mysql_database", Constants.MYSQL_DATABASE);
        settingsImplementation.addParameter("mysql_username", Constants.MYSQL_USERNAME);
        settingsImplementation.addParameter("mysql_password", Constants.MYSQL_PASSWORD);
        return settingsImplementation;
    }


    public void readDataFromTable(String fromTable){
        tableModel.setRows(this.database.getDataFromTable(fromTable));
    }




}
