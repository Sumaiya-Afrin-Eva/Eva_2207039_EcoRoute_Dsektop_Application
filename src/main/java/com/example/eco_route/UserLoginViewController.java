package com.example.eco_route;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class UserLoginViewController {

    @FXML private ImageView backHome;
    @FXML private Button emailButton;

    private HomeController homeController;

    @FXML
    public void initialize() {
        backHome.setOnMouseClicked(e -> onBackHome());
        emailButton.setOnAction(e -> openEmailLoginOverlay());
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    private void onBackHome() {
        if (homeController != null) {
            homeController.closeOverlay();
        }
    }

    private void openEmailLoginOverlay() {
        if (homeController == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("email_login_view.fxml"));
            Node ui = loader.load();

            EmailLoginViewController controller = loader.getController();
            controller.setHomeController(homeController);

            ui.setStyle("-fx-background-color: white;-fx-background-radius: 15;");

            double popupWidth = 500;
            double popupHeight = 600;

            AnchorPane rootPane = homeController.getRootPane();

            ui.setLayoutX((rootPane.getWidth() - popupWidth) / 2);
            ui.setLayoutY((rootPane.getHeight() - popupHeight) / 2);
            ui.resize(popupWidth, popupHeight);

            homeController.getOverlayPane().getChildren().clear();
            homeController.getOverlayPane().getChildren().add(ui);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
