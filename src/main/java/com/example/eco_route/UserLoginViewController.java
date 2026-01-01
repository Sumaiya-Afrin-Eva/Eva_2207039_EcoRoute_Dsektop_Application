package com.example.eco_route;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class UserLoginViewController {

    @FXML private ImageView back;
    @FXML private Button emailButton;

    private HomeController homeController;

    @FXML
    public void initialize() {
        System.out.println("UserLoginViewController initialize started");

        if (back != null) {
            back.setOnMouseClicked(e -> {
                System.out.println("Back button clicked");
                onBackHome();
            });
            back.setCursor(javafx.scene.Cursor.HAND);
            System.out.println("Back button handler attached");
        } else {
            System.out.println("ERROR: back ImageView is NULL!");
        }

        if (emailButton != null) {
            emailButton.setOnAction(e -> {
                System.out.println("Email button clicked");
                openEmailLoginOverlay();
            });
            System.out.println("Email button handler attached");
        } else {
            System.out.println("ERROR: emailButton is NULL!");
        }

        System.out.println("UserLoginViewController initialize complete");
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
        System.out.println("Home controller set in UserLoginViewController");
    }

    private void onBackHome() {
        System.out.println("onBackHome called");
        if (homeController != null) {
            homeController.closeOverlay();
        }
    }

    private void openEmailLoginOverlay() {
        System.out.println("openEmailLoginOverlay called");

        if (homeController == null) {
            System.out.println("ERROR: homeController is NULL");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("email_login_view.fxml"));
            Node ui = loader.load();

            EmailLoginViewController controller = loader.getController();

            if (controller != null) {
                controller.setHomeController(homeController);
                System.out.println("EmailLoginViewController controller set");
            } else {
                System.out.println("ERROR: EmailLoginViewController controller is NULL");
            }

            ui.setStyle("-fx-background-color: white;-fx-background-radius: 15;");

            double popupWidth = 500;
            double popupHeight = 600;

            AnchorPane rootPane = homeController.getRootPane();
            ui.setLayoutX((rootPane.getWidth() - popupWidth) / 2);
            ui.setLayoutY((rootPane.getHeight() - popupHeight) / 2);
            ui.resize(popupWidth, popupHeight);

            homeController.getOverlayPane().getChildren().clear();
            homeController.getOverlayPane().getChildren().add(ui);

            System.out.println("Email login overlay opened successfully");

        } catch (IOException e) {
            System.out.println("Error opening email login overlay");
            e.printStackTrace();
        }
    }
}