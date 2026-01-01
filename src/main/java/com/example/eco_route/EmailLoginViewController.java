package com.example.eco_route;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;


public class EmailLoginViewController {

    @FXML
    private ImageView backHome;

    @FXML
    private TextField emailBtn;

    @FXML
    private Button nextBtn;

    private HomeController homeController;

    @FXML
    public void initialize() {
        backHome.setOnMouseClicked(e -> loadHomeView());
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    private void loadHomeView() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("home-view.fxml")
            );
            Stage stage = (Stage) backHome.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot load Home page");
        }
    }

    @FXML
    private void handleNext(ActionEvent event) {
        String email = emailBtn.getText().trim();

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email",
                    "Please enter a valid email address.");
            return;
        }

        String otp = generateOTP();

        if (!sendEmail(email, otp)) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to send verification code.");
            return;
        }

        openVerificationView(email, otp);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private boolean sendEmail(String receiver, String otp) {

        final String senderEmail = "sumaiya.afrin.evaa@gmail.com";
        final String senderPassword = "mlxh cuso hwfm absv";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiver)
            );
            message.setSubject("Eco-Route Verification Code");
            message.setText(
                    "Your Eco-Route verification code is:\n\n" +
                            otp + "\n\nThis code will expire shortly."
            );

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openVerificationView(String email, String otp) {
        if (homeController == null) {
            return;
        }

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("code_verification_view.fxml"));
            Node ui = loader.load();

            CodeVerificationViewController controller = loader.getController();
            controller.setData(email, otp);
            controller.setHomeController(homeController);

            ui.setStyle("-fx-background-color: white;-fx-background-radius: 15;");

            double popupWidth = 500;
            double popupHeight = 600;

            AnchorPane rootPane = homeController.getRootPane();
            Pane overlayPane = homeController.getOverlayPane();

            ui.setLayoutX((rootPane.getWidth() - popupWidth) / 2);
            ui.setLayoutY((rootPane.getHeight() - popupHeight) / 2);
            ui.resize(popupWidth, popupHeight);

            overlayPane.getChildren().clear();
            overlayPane.getChildren().add(ui);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}