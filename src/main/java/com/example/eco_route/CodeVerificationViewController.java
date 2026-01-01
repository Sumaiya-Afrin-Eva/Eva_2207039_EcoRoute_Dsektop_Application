package com.example.eco_route;

import com.example.eco_route.service.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeVerificationViewController {

    @FXML
    private ImageView backHome;

    @FXML
    private Label email;

    @FXML
    private TextField code1;

    @FXML
    private TextField code2;

    @FXML
    private TextField code3;

    @FXML
    private TextField code4;

    @FXML
    private TextField code5;

    @FXML
    private TextField code6;

    @FXML
    private Button verifyCode;

    private String sentCode;
    private String userEmail;

    private HomeController homeController;

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    public void setData(String userEmailAddress, String verificationCode) {
        userEmail = userEmailAddress;
        email.setText(userEmailAddress);
        this.sentCode = verificationCode;
    }

    @FXML
    public void initialize() {
        backHome.setOnMouseClicked(e -> loadHome());

        addAutoFocus(code1, code2);
        addAutoFocus(code2, code3);
        addAutoFocus(code3, code4);
        addAutoFocus(code4, code5);
        addAutoFocus(code5, code6);
    }

    @FXML
    private void handleVerify() {
        String enteredCode =
                code1.getText() +
                        code2.getText() +
                        code3.getText() +
                        code4.getText() +
                        code5.getText() +
                        code6.getText();

        if (enteredCode.equals(sentCode)) {
            UserSessionManager.getInstance().setCurrentUserEmail(userEmail);
            UserSessionManager.getInstance().setCurrentUserId(userEmail);

            openUserView();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Invalid Code",
                    "The verification code you entered is incorrect.");
        }
    }

    private void openUserView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("user_view.fxml")
            );

            Parent root = loader.load();
            Stage stage = (Stage) verifyCode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error", "Unable to load user view.");
        }
    }

    private void loadHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("home_view.fxml")
            );

            Parent root = loader.load();
            Stage stage = (Stage) backHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error", "Unable to load home view.");
        }
    }

    private void addAutoFocus(TextField current, TextField next) {
        current.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 1) {
                next.requestFocus();
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}