package com.example.eco_route;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManageRoutesController {

    @FXML
    private ComboBox<String> routeComboBox;

    @FXML
    private TextField originField, destinationField, fuelField, timeField, ticketField;

    @FXML
    private Label statusLabel;
    @FXML private Button back;

    private Map<String, Route> routeMap = new HashMap<>();

    @FXML
    public void initialize() {

        if (back != null) {
            back.setOnAction(e -> back());
        }

        routeMap.put("Dhaka → Khulna", new Route("Dhaka", "Khulna", 12, 2.5, 500));
        routeMap.put("Dhaka → Chittagong", new Route("Dhaka", "Chittagong", 15, 3.2, 700));

        routeComboBox.getItems().addAll(routeMap.keySet());
    }

    @FXML
    private void loadSelectedRoute() {
        String key = routeComboBox.getValue();
        if (key == null) return;

        Route r = routeMap.get(key);

        originField.setText(r.origin);
        destinationField.setText(r.destination);
        fuelField.setText(String.valueOf(r.fuel));
        timeField.setText(String.valueOf(r.time));
        ticketField.setText(String.valueOf(r.ticket));
    }

    @FXML
    private void updateRoute() {
        String key = routeComboBox.getValue();
        if (key == null) {
            statusLabel.setText("Please select a route first.");
            return;
        }

        try {
            Route updated = new Route(
                    originField.getText(),
                    destinationField.getText(),
                    Double.parseDouble(fuelField.getText()),
                    Double.parseDouble(timeField.getText()),
                    Double.parseDouble(ticketField.getText())
            );

            routeMap.put(key, updated);
            statusLabel.setText("Route updated successfully!");

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid numeric input!");
        }
    }

    @FXML
    private void resetFields() {
        originField.clear();
        destinationField.clear();
        fuelField.clear();
        timeField.clear();
        ticketField.clear();
        statusLabel.setText("");
    }

    static class Route {
        String origin, destination;
        double fuel, time, ticket;

        Route(String origin, String destination, double fuel, double time, double ticket) {
            this.origin = origin;
            this.destination = destination;
            this.fuel = fuel;
            this.time = time;
            this.ticket = ticket;
        }
    }

    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("admin_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) back.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to load home view.").show();
        }
    }
}
