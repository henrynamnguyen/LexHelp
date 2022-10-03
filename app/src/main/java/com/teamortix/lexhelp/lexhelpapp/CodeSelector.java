package com.teamortix.lexhelp.lexhelpapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CodeSelector {
    public static Stage dialog;
    public TextField input;


    public static void init() {
        try {
            dialog = new Stage();
            Parent root = FXMLLoader.load(
                    LexHelp.class.getResource("InputModal.fxml")
            );
            dialog.setScene(new Scene(root));
            dialog.setTitle("Load your PDF");
            dialog.initModality(Modality.WINDOW_MODAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(ObservableList<String> update) {
        dialog.showAndWait();
        System.out.println("done");
        update.add("active");
    }


    public void load(ActionEvent e) {
        dialog.close();
    }
}
