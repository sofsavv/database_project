package controller;

import model.parser.QueryParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunAction implements ActionListener {

    private JTextArea textArea;

    public RunAction(JTextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        QueryParser parser = new QueryParser();
        String query = textArea.getText();
        parser.parse(query.replace(","," , ")
                .replace(">="," greater_equal ")
                .replace("!="," not ")
                .replace("<="," less_equal ")
                .replace("="," equals ")
                .replace(">"," greater ")
                .replace("<"," less ")
                .replace("not in"," not_in "));

    }

}
