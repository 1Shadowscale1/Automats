package org.openjfx.Controllers;

import algorithms.BruteforceStrMatcher;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static org.openjfx.Controllers.SolveTaskInputController.inputData;

public class TaskFiveController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        TableView<List<String>> tableView = createTableView(new ArrayList<>(BruteforceStrMatcher.Bruteforce(inputData.get(0), inputData.get(1))));

        Text tableInfo = new Text("Протокол отработки алгоритма");
        tableInfo.setFill(Color.WHITESMOKE);
        tableInfo.setFont(Font.font("System", 20));

        Button returnToStartButton = new Button("Вернуться в начало");
        setupButtonAsReturnToStart(returnToStartButton);

        setupMainPaneForBruteForceTableViewDisplay(mainPane, tableView, tableInfo, returnToStartButton);
    }

    public static TableView<List<String>> createTableView(List<String> result) {
        List<List<String>> items = new ArrayList<>();


        for (String s : result) {
            String[] currentResult = s.trim().split("\\s{5}");
            if (currentResult.length > 1) {
                items.add(new ArrayList<>());
                int lastIndex = items.size() - 1;
                items.get(lastIndex).add(currentResult[0]);
                items.get(lastIndex).add(currentResult[1]);
                items.get(lastIndex).add("");
            } else {
                int lastIndex = items.size() - 1;
                items.get(lastIndex).remove(2);
                items.get(lastIndex).add(currentResult[0]);
            }
        }

        ObservableList<List<String>> data = FXCollections.observableArrayList(items);
        TableView<List<String>> tableView = new TableView<>();

        int stateColumnWidth = 150;
        int regularColumnWidth = 75;
        int width;

        for (int i = 0; i < 3; i++) {
            TableColumn<List<String>, String> tableColumn;
            if (i == 0 || i == 1) {
                if (i == 0) {
                    tableColumn = new TableColumn<>("Подстрока");
                } else {
                    tableColumn = new TableColumn<>("Текст");
                }
                width = regularColumnWidth;
            } else {
                tableColumn = new TableColumn<>("Состояние");
                width = stateColumnWidth;
            }
            tableColumn.setMinWidth(width * 3);
            tableColumn.setPrefWidth(width * 3);
            tableColumn.setMaxWidth(width * 3);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue().get(columnNumber))));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
            tableView.getColumns().add(tableColumn);
        }

        tableView.setItems(data);
        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(34));
        tableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(tableView.getFixedCellSize()).add(34));
        tableView.prefWidthProperty().bind(new SimpleIntegerProperty(stateColumnWidth + regularColumnWidth * 10));
        tableView.maxWidthProperty().bind(new SimpleIntegerProperty(stateColumnWidth + regularColumnWidth * 10));

        return tableView;
    }

    public static void setupMainPaneForBruteForceTableViewDisplay(AnchorPane mainPane, TableView<List<String>> tableView, Text tableInfo, Button button) {
        mainPane.getChildren().addAll(tableView, tableInfo, button);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(tableView, 25.0);
        AnchorPane.setTopAnchor(tableView, 50.0);

        AnchorPane.setLeftAnchor(tableInfo, 25.0);
        AnchorPane.setTopAnchor(tableInfo, 10.0);

        AnchorPane.setLeftAnchor(button, 10.0);
        AnchorPane.setBottomAnchor(button, 10.0);
    }

    public static void setupButtonAsReturnToStart(Button button) {
        button.setOnAction(event -> {
            inputData.clear();
            button.getScene().getWindow().hide();
            Loader.loadFxmlStartupPage();
        });
    }
}
