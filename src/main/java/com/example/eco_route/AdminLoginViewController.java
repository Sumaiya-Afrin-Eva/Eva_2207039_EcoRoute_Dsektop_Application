package com.example.eco_route;

import com.example.eco_route.service.CompanyService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginViewController {

    @FXML private ScrollPane rootPane;

    @FXML private TextField businessName;
    @FXML private TextField ownerName;
    @FXML private TextField vehicleType;
    @FXML private TextField businessEmail;
    @FXML private TextField phoneNumber;

    @FXML private Button registerButton;
    @FXML private Button loginLabel;
    @FXML private ImageView backHome;

    private final CompanyService companyService = CompanyService.getInstance();

    @FXML
    public void initialize() {

        registerButton.setOnAction(e -> {
            saveCompanyName();
            openScene("admin_view.fxml");
        });

        loginLabel.setOnAction(e -> openScene("email_login_view.fxml"));

        backHome.setOnMouseClicked(e -> openScene("home_view.fxml"));
    }

    private void saveCompanyName() {
        String company = businessName.getText();

        if (company != null && !company.isBlank()) {
            companyService.addCompany(company);
        }
    }

    private void openScene(String fxmlFile) {
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
}
