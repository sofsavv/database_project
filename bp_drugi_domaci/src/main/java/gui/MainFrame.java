package gui;

import app.AppCore;
import lombok.Data;

import javax.swing.*;

import java.awt.*;


@Data
public class MainFrame extends JFrame {

    private static MainFrame instance = null;
    private AppCore appCore;
    private JButton runBtn;
    private JTextArea textArea;
    private JTable jTable;

    private MainFrame() {

    }

    public static MainFrame getInstance(){
        if (instance==null){
            instance=new MainFrame();
            instance.initialise();
        }
        return instance;
    }


    private void initialise() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jTable = new JTable();
//        jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        jTable.setFillsViewportHeight(true);
        setPreferredSize(new Dimension(700, 500));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        runBtn = new JButton();
        runBtn.setIcon(new ImageIcon("run.png"));

        buttonPanel.setAlignmentX(getAlignmentX());
        buttonPanel.add(runBtn);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        textArea = new JTextArea(5,40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        JScrollPane tableScrollPane = new JScrollPane();
        tableScrollPane.add(jTable);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
        pack();

    }

    public void setAppCore(AppCore appCore) {
        this.appCore = appCore;
        this.jTable.setModel(appCore.getTableModel());
    }

}
