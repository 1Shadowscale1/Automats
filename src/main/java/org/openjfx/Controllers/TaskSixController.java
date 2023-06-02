package org.openjfx.Controllers;

import algorithms.KnuthMorrisPratt;
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
import java.util.Objects;

import static org.openjfx.Controllers.SolveTaskInputController.inputData;

public class TaskSixController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        List<String> result = new ArrayList<>(KnuthMorrisPratt.KMP(inputData.get(0), inputData.get(1)));
        List<String> newResult = new ArrayList<>(result);
        newResult.remove(0);
        newResult.remove(0);
        TableView<List<String>> tableView = TaskFiveController.createTableView(newResult);
        TableView<List<String>> prefixFunctionView = createTableView(result);

        Text tableInfo = new Text("Протокол отработки алгоритма");
        tableInfo.setFill(Color.WHITESMOKE);
        tableInfo.setFont(Font.font("System", 20));

        Text prefixFunctionInfo = new Text("Префикс-функция");
        prefixFunctionInfo.setFill(Color.WHITESMOKE);
        prefixFunctionInfo.setFont(Font.font("System", 20));

        Button returnToStartButton = new Button("Вернуться в начало");
        setupButtonAsReturnToStart(returnToStartButton);

        setupMainPaneForBruteForceTableViewDisplay(mainPane, tableView, prefixFunctionView, tableInfo, prefixFunctionInfo, returnToStartButton);
    }

    public static TableView<List<String>> createTableView(List<String> result) {
        List<List<String>> items = new ArrayList<>();
        String[] firstPrefixData = result.get(0).split("\\) ")[1].split("\\D+");
        String[] rawSecondPrefixData = result.get(1).split("\\) ")[1].split("[^A-Za-z]+");
        String[] secondPrefixData = new String[firstPrefixData.length];
        secondPrefixData[0] = "";

        int counter = 1;
        for (int i = 1; i < firstPrefixData.length; i++) {
            if (Objects.equals(firstPrefixData[i], "0")) {
                secondPrefixData[i] = "";
            } else {
                secondPrefixData[i] = rawSecondPrefixData[counter];
                counter++;
            }
        }

        for (int i = 1; i < firstPrefixData.length; i++) {
            items.add(new ArrayList<>());
            items.get(i - 1).add(String.valueOf(i - 1));
            items.get(i - 1).add(firstPrefixData[i]);
            items.get(i - 1).add(secondPrefixData[i]);
        }

        ObservableList<List<String>> data = FXCollections.observableArrayList(items);
        TableView<List<String>> tableView = new TableView<>();

        int regularColumnWidth = 25;
        int wordColumnWidth = 75;
        int width;

        for (int i = 0; i < 3; i++) {
            TableColumn<List<String>, String> tableColumn;
            if (i == 1 || i == 2) {
                if (i == 1) {
                    tableColumn = new TableColumn<>("Префикс-функция");
                } else {
                    tableColumn = new TableColumn<>("Префикс = суффикс");
                }
                width = wordColumnWidth;
            } else {
                tableColumn = new TableColumn<>("Шаг");
                width = regularColumnWidth;
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
        tableView.prefWidthProperty().bind(new SimpleIntegerProperty(wordColumnWidth + regularColumnWidth * 18));
        tableView.maxWidthProperty().bind(new SimpleIntegerProperty(wordColumnWidth + regularColumnWidth * 18));

        return tableView;
    }

    public static void setupMainPaneForBruteForceTableViewDisplay(AnchorPane mainPane, TableView<List<String>> tableView, TableView<List<String>> prefixFunctionView,  Text tableInfo, Text prefixFunctionInfo, Button button) {
        mainPane.getChildren().addAll(tableView, prefixFunctionView, tableInfo, prefixFunctionInfo, button);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(tableView, 25.0);
        AnchorPane.setTopAnchor(tableView, 50.0);

        AnchorPane.setLeftAnchor(tableInfo, 25.0);
        AnchorPane.setTopAnchor(tableInfo, 10.0);

        AnchorPane.setRightAnchor(prefixFunctionView, 25.0);
        AnchorPane.setTopAnchor(prefixFunctionView, 50.0);

        AnchorPane.setRightAnchor(prefixFunctionInfo, 25.0);
        AnchorPane.setTopAnchor(prefixFunctionInfo, 10.0);

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
