package com.example.eco_route;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML private AnchorPane rootPane;
    @FXML private Pane overlayPane;

    @FXML private Label home;
    @FXML private Label aboutUs;
    @FXML private Label service;
    @FXML private Label contact;

    @FXML private Button login;
    @FXML private Button routePartnerBtn;

    @FXML
    public void initialize() {
        home.setOnMouseClicked(e -> openOverlay("home.fxml"));
        aboutUs.setOnMouseClicked(e -> openOverlay("about.fxml"));
        service.setOnMouseClicked(e -> openOverlay("service.fxml"));
        contact.setOnMouseClicked(e -> openOverlay("contact.fxml"));

        login.setOnAction(e -> openUserLoginOverlay());
        routePartnerBtn.setOnAction(e -> openNewScene("admin_login_view.fxml"));
    }

    private void openOverlay(String file) {
        try {
            Node ui = FXMLLoader.load(getClass().getResource(file));

            ui.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;"
            );

            double popupWidth  = 500;
            double popupHeight = 600;

            ui.setLayoutX((rootPane.getWidth()  - popupWidth)  / 2);
            ui.setLayoutY((rootPane.getHeight() - popupHeight) / 2);
            ui.resize(popupWidth, popupHeight);

            overlayPane.getChildren().clear();
            overlayPane.getChildren().add(ui);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openUserLoginOverlay() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("user_login_view.fxml"));
            Node ui = loader.load();

            UserLoginViewController controller = loader.getController();
            controller.setHomeController(this);

            ui.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;"
            );

            double popupWidth  = 500;
            double popupHeight = 700;

            ui.setLayoutX((rootPane.getWidth()  - popupWidth)  / 2);
            ui.setLayoutY((rootPane.getHeight() - popupHeight) / 2);
            ui.resize(popupWidth, popupHeight);

            overlayPane.getChildren().clear();
            overlayPane.getChildren().add(ui);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openNewScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxmlFile)
            );
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) rootPane.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOverlay() {
        overlayPane.getChildren().clear();
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    public Pane getOverlayPane() {
        return overlayPane;
    }
}