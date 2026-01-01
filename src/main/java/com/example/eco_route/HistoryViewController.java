package com.example.eco_route;

import com.example.eco_route.database.SearchHistoryDAO;
import com.example.eco_route.service.UserSessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HistoryViewController {

    @FXML private VBox historyContainer;
    @FXML private Button backButton;
    @FXML private Button deleteAllButton;

    private final SearchHistoryDAO searchHistoryDAO = SearchHistoryDAO.getInstance();
    private final UserSessionManager sessionManager = UserSessionManager.getInstance();

    @FXML
    public void initialize() {
        loadSearchHistory();
    }

    private void loadSearchHistory() {
        String userEmail = sessionManager.getCurrentUserEmail();

        if (userEmail == null || userEmail.isEmpty()) {
            Label errorLabel = new Label("No user session found. Please login again.");
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #d32f2f;");
            historyContainer.getChildren().add(errorLabel);
            return;
        }

        List<SearchHistoryDAO.SearchHistoryRecord> history = searchHistoryDAO.getUserSearchHistory(userEmail);

        if (history.isEmpty()) {
            Label emptyLabel = new Label("No search history found for your account.");
            emptyLabel.setStyle("-fx-font-size: 16px;");
            historyContainer.getChildren().add(emptyLabel);

            if (deleteAllButton != null) {
                deleteAllButton.setDisable(true);
            }
            return;
        }

        if (deleteAllButton != null) {
            deleteAllButton.setDisable(false);
        }

        Label userLabel = new Label("Search History for: " + userEmail);
        userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-style: italic;");
        historyContainer.getChildren().add(userLabel);

        for (SearchHistoryDAO.SearchHistoryRecord record : history) {
            VBox historyCard = createHistoryCard(record);
            historyContainer.getChildren().add(historyCard);
        }
    }

    private VBox createHistoryCard(SearchHistoryDAO.SearchHistoryRecord record) {
        VBox card = new VBox();
        card.setStyle(
                "-fx-border-color: #cccccc; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-color: #f9f9f9; " +
                        "-fx-padding: 15; " +
                        "-fx-border-width: 1;"
        );
        card.setSpacing(12);

        Label routeLabel = new Label(record.origin + " → " + record.destination);
        routeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #0015ff;");

        HBox companyRow = createInfoRow("Company:", record.selectedCompany);
        HBox distanceRow = createInfoRow("Distance:", record.distance + " km");
        HBox timeRow = createInfoRow("Time:", record.time + " hrs");
        HBox fuelRow = createInfoRow("Fuel:", record.fuel + " L");
        HBox ecoRow = createInfoRow("Eco Score:", String.format("%.2f", record.ecoScore));
        ecoRow.setStyle("-fx-text-fill: #049029; -fx-font-weight: bold;");
        HBox costRow = createInfoRow("Cost:", "৳ " + record.ticket);
        HBox dateRow = createInfoRow("Searched At:", record.searchedAt.toString());

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle(
                "-fx-padding: 8 15 8 15; " +
                        "-fx-font-size: 14; " +
                        "-fx-background-color: #d32f2f; " +
                        "-fx-text-fill: white; " +
                        "-fx-cursor: hand; " +
                        "-fx-border-radius: 5;"
        );
        deleteButton.setOnAction(e -> deleteHistoryRecord(record.id));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(deleteButton);

        card.getChildren().addAll(
                routeLabel,
                companyRow,
                distanceRow,
                timeRow,
                fuelRow,
                ecoRow,
                costRow,
                dateRow,
                new HBox(),
                buttonBox
        );

        return card;
    }

    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox();
        row.setSpacing(10);

        Label labelText = new Label(label);
        labelText.setFont(new Font(16));
        labelText.setStyle("-fx-font-weight: bold;");

        Label valueText = new Label(value);
        valueText.setFont(new Font(16));

        row.getChildren().addAll(labelText, valueText);
        return row;
    }

    @FXML
    private void deleteHistoryRecord(int historyId) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this search history?");

        if (confirmAlert.showAndWait().isPresent() &&
                confirmAlert.getResult().getButtonData().isDefaultButton()) {
            searchHistoryDAO.deleteSearchHistory(historyId);
            showAlert(Alert.AlertType.INFORMATION, "Success", "History record deleted successfully!");

            historyContainer.getChildren().clear();
            loadSearchHistory();
        }
    }

    @FXML
    private void deleteAllHistory() {
        String userEmail = sessionManager.getCurrentUserEmail();

        if (userEmail == null || userEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "No user session found.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete All");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete ALL search history? This action cannot be undone.");

        if (confirmAlert.showAndWait().isPresent() &&
                confirmAlert.getResult().getButtonData().isDefaultButton()) {
            searchHistoryDAO.deleteAllUserHistory(userEmail);
            showAlert(Alert.AlertType.INFORMATION, "Success", "All history records deleted successfully!");

            historyContainer.getChildren().clear();
            loadSearchHistory();
        }
    }

    @FXML
    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("user_view.fxml")
            );

            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) historyContainer.getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load user view.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}