package com.teamortix.lexhelp.lexhelpapp;

import java.io.File;
import java.io.FileInputStream;

import awslextest.Lex;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LexHelp extends Application {
    @Override
    public void start(Stage primaryStage) {
        int width = 1728, height = 646;
        try {
            AnchorPane root = new AnchorPane();
            root.setId("root");
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(LexHelp.class.getResource("application.css").toExternalForm());

            // icon
            File f = new File("entypo_modern-mic.png");
            Image icon = new Image(new FileInputStream(f));

            // mic button
            FileInputStream micBtnPath = new FileInputStream("mic.png");
            Image micBtnImg = new Image(micBtnPath);
            ImageView micBtnImgView = new ImageView(micBtnImg);
            Button micBtn = new Button("", micBtnImgView);
            micBtn.setTranslateY(14);
            micBtn.setId("mic-Button");
            micBtn.setAccessibleHelp("Start Talking To Amazon Lex");

            Lex l = new Lex();
            micBtn.setOnAction((event) -> {
                System.out.println("Button clicked!");
                try {
                    l.begin();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
            AnchorPane.setRightAnchor(micBtn, width / 2d - 45); // distance 0 from right side of
            AnchorPane.setTopAnchor(micBtn, height / 2d - 45);
            root.getChildren().add(micBtn);

            // upload buttons
            Button uploadPDFBtn = new Button("Open New PDF");
            uploadPDFBtn.setId("uploadPDFBtn");
            uploadPDFBtn.getStyleClass().add("buttons");
            uploadPDFBtn.setAccessibleHelp("Open New PDF Button");
            CodeSelector.init();
            uploadPDFBtn.setOnMouseClicked(e -> {
                CodeSelector.show(micBtn.getStyleClass());
            });

            FileInputStream onboardImgPath = new FileInputStream("src/main/resources/com/teamortix/lexhelp/lexhelpapp/images/onboarding_preview.png");
            Image onboardImg = new Image(onboardImgPath);
            ImageView onboardImgView = new ImageView(onboardImg);
            onboardImgView.setId("file-preview");
            onboardImgView .setFitHeight(165);
            onboardImgView .setFitWidth(150);
            Button button1 = new Button("Onboarding.pdf", onboardImgView);
            Tooltip.install(button1, new Tooltip("Onboarding.pdf"));
            button1.setContentDisplay(ContentDisplay.TOP);
            button1.setId("uploadedBtn");
            button1.getStyleClass().add("buttons");
            button1.setAccessibleHelp("Onboarding.pdf");

            FileInputStream ndaImgPath = new FileInputStream("src/main/resources/com/teamortix/lexhelp/lexhelpapp/images/nda_preview.png");
            Image ndaImg = new Image(ndaImgPath);
            ImageView ndaImgView = new ImageView(ndaImg);
            ndaImgView.setId("file-preview");
            ndaImgView.setFitHeight(165);
            ndaImgView.setFitWidth(170);
            Button button2 = new Button("NDA__agreement.pdf", ndaImgView);
            Tooltip.install(button2, new Tooltip("NDA__agreement.pdf"));
            button2.setContentDisplay(ContentDisplay.TOP);
            button2.setId("uploadedBtn");
            button2.getStyleClass().add("buttons");
            button1.setAccessibleHelp("NDA_agreement.pdf");

            HBox buttonsPanel = new HBox(uploadPDFBtn, button1, button2);
            buttonsPanel.setId("buttonsPanel");
            AnchorPane.setBottomAnchor(buttonsPanel, (height/124d));
            AnchorPane.setLeftAnchor(buttonsPanel, width/4d);
            root.getChildren().add(buttonsPanel);

            primaryStage.setTitle("LexHelp");
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
