package com.example.eco_route;

import com.example.eco_route.model.Route;
import com.example.eco_route.service.RouteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserViewController {

    @FXML private Button info;
    @FXML private Button home;
    @FXML private Button history;
    @FXML private ComboBox<String> profile;
    @FXML private ImageView profileImage;

    @FXML private ImageView car;
    @FXML private ImageView bike;
    @FXML private ImageView bus;
    @FXML private ImageView walk;

    @FXML private TextField origin;
    @FXML private TextField destination;

    @FXML private WebView web;

    @FXML private VBox routesContainer;

    private final RouteService routeService = RouteService.getInstance();

    @FXML
    public void initialize() {
        if (routesContainer != null) {
            routesContainer.setSpacing(15);
            routesContainer.setPadding(new Insets(15));
        }

        if (home != null) {
            home.setOnAction(e -> goHome());
        }
    }

    @FXML
    private void findRoutes() {
        String originText = origin.getText().trim();
        String destinationText = destination.getText().trim();

        if (originText.isEmpty() || destinationText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both origin and destination.");
            return;
        }

        List<Route> routes = routeService.getRoutes(originText, destinationText);

        if (routes == null || routes.isEmpty()) {
            clearResults();
            showAlert(Alert.AlertType.INFORMATION, "No Routes Found",
                    "No routes available for the selected origin and destination.");
            return;
        }

        displayRoutes(routes);
    }

    private void displayRoutes(List<Route> routes) {
        if (routesContainer == null) {
            return;
        }

        routesContainer.getChildren().clear();

        for (Route route : routes) {
            VBox routeCard = createRouteCard(route);
            routesContainer.getChildren().add(routeCard);
        }
    }

    private VBox createRouteCard(Route route) {
        VBox card = new VBox();
        card.setStyle(
                "-fx-border-color: #cccccc; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-color: #f9f9f9; " +
                        "-fx-padding: 15; " +
                        "-fx-border-width: 1;"
        );
        card.setSpacing(12);

        Label companyLabel = new Label(route.getCompany());
        companyLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #0015ff;");

        HBox routeRow = createInfoRow("Route:",
                route.getRoutePath() != null ? route.getRoutePath() : (route.getOrigin() + " → " + route.getDestination()));

        HBox distanceRow = createInfoRow("Distance:", route.getDistance() + " km");

        HBox timeRow = createInfoRow("Time:", route.getTime() + " hrs");

        HBox fuelRow = createInfoRow("Fuel:", route.getFuel() + " L");

        HBox ecoRow = createInfoRow("Eco Score:", String.format("%.2f", route.getEcoScore()));
        ecoRow.setStyle("-fx-text-fill: #049029; -fx-font-weight: bold;");

        HBox costRow = createInfoRow("Cost:", "৳ " + route.getTicket());

        card.getChildren().addAll(
                companyLabel,
                routeRow,
                distanceRow,
                timeRow,
                fuelRow,
                ecoRow,
                costRow
        );
        return card;
    }

    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox();
        row.setSpacing(10);

        Label labelText = new Label(label);
        labelText.setFont(new Font(18));
        labelText.setStyle("-fx-font-weight: bold;");

        Label valueText = new Label(value);
        valueText.setFont(new Font(18));

        row.getChildren().addAll(labelText, valueText);
        return row;
    }

    private void clearResults() {
        if (routesContainer != null) {
            routesContainer.getChildren().clear();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void goHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("home_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) home.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load home view.");
        }
    }
}