package org.openjfx.Controllers;

import static org.openjfx.Controllers.Controller.automatonList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;

import automaton.Automaton;
import automaton.NFAutomaton;

public class TaskEightController {
    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton automaton = automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(automaton);

        NFAutomaton reverseAutomaton = (NFAutomaton) automatonList.get(1);
        TableView<String[]> reverseAutomatonTableView = createNFAutomatonJumpTableTableView(reverseAutomaton);

        Text automatonInfo = new Text("Исходный автомат");
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Text reverseAutomatonInfo = new Text("Реверс исходного автомата");
        reverseAutomatonInfo.setFill(Color.WHITESMOKE);
        reverseAutomatonInfo.setFont(Font.font("System", 20));

        var returnToStartButton = new Button("Вернуться в начало");
        setupButtonAsReturnToStart(returnToStartButton);

        Button transformNFA2DFAButton = getDFAButton(reverseAutomaton);

        setupMainPaneForAutomatonsTableViewDisplay(mainPane, automatonTableView, reverseAutomatonTableView,
                automatonInfo, reverseAutomatonInfo, returnToStartButton, transformNFA2DFAButton);
    }

    public static void setupButtonAsReturnToStart(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxmlStartupPage();
        });
    }

    public static void setupMainPaneForAutomatonsTableViewDisplay(AnchorPane mainPane,
            TableView<String[]> firstAutomatonTableView, TableView<String[]> secondAutomatonTableView,
            Text firstAutomatonInfo, Text secondAutomatonInfo, Button firstButton, Button secondButton) {
        mainPane.getChildren().addAll(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo,
                secondAutomatonInfo, firstButton, secondButton);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setRightAnchor(secondAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(secondAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonInfo, 10.0);

        AnchorPane.setBottomAnchor(firstButton, 10.0);
        AnchorPane.setLeftAnchor(firstButton, 10.0);

        AnchorPane.setBottomAnchor(secondButton, 10.0);
        AnchorPane.setRightAnchor(secondButton, 10.0);
    }

    public static TableView<String[]> createNFAutomatonJumpTableTableView(NFAutomaton nfAutomaton) {
        String[][] jumpTable = new String[nfAutomaton.jumpTable.rowMap().size()][nfAutomaton.letters.size() + 1];

        String[] arr = nfAutomaton.jumpTable.rowKeySet().toArray(new String[0]);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < nfAutomaton.letters.size() + 1; j++) {
                if (j == 0) {
                    boolean isStart = false;
                    boolean isFinal = false;
                    if (nfAutomaton.startVertices.contains(arr[i]))
                        isStart = true;
                    if (nfAutomaton.finalVertices.contains(arr[i]))
                        isFinal = true;

                    if (!isStart && !isFinal) {
                        jumpTable[i][j] = arr[i];
                    } else if (isStart && !isFinal) {
                        jumpTable[i][j] = "-> " + arr[i] + "    ";
                    } else if (!isStart) {
                        jumpTable[i][j] = "<- " + arr[i] + "    ";
                    } else {
                        jumpTable[i][j] = "<- " + arr[i] + " ->";
                    }
                } else {
                    jumpTable[i][j] = getName(nfAutomaton.jumpTable.get(arr[i], nfAutomaton.letters.get(j - 1)));
                }
            }
        }

        Arrays.sort(jumpTable, (arr1, arr2) -> {
            try {
                String replacedArr1 = arr1[0].replaceAll("<-", "").replaceAll("->", "").strip();
                String replacedArr2 = arr2[0].replaceAll("<-", "").replaceAll("->", "").strip();
                return Integer.compare(Integer.parseInt(replacedArr1), Integer.parseInt(replacedArr2));
            } catch (NumberFormatException e) {
                return arr1[0].compareTo(arr2[0]);
            }
        });

        ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);
        TableView<String[]> automatonTableView = new TableView<>();

        int stateColumnWidth = 150;
        int regularColumnWidth = 50;
        int width;

        for (int i = 0; i < nfAutomaton.letters.size() + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Состояние \\ Буква");
                width = stateColumnWidth;
            } else {
                tableColumn = new TableColumn<>(nfAutomaton.letters.get(i - 1).strip());
                width = regularColumnWidth;
            }
            tableColumn.setMinWidth(width);
            tableColumn.setPrefWidth(width);
            tableColumn.setMaxWidth(width);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle("-fx-alignment: CENTER;");
            automatonTableView.getColumns().add(tableColumn);
        }

        automatonTableView.setItems(data);
        automatonTableView.setFixedCellSize(25);
        automatonTableView.prefHeightProperty().bind(
                Bindings.size(automatonTableView.getItems()).multiply(automatonTableView.getFixedCellSize()).add(34));
        automatonTableView.maxHeightProperty()
                .bind((new SimpleIntegerProperty(20)).multiply(automatonTableView.getFixedCellSize()).add(34));
        automatonTableView.prefWidthProperty()
                .bind(new SimpleIntegerProperty(stateColumnWidth + regularColumnWidth * 4));
        automatonTableView.maxWidthProperty()
                .bind(new SimpleIntegerProperty(stateColumnWidth + regularColumnWidth * 4));

        return automatonTableView;
    }

    public static String getName(List<String> rawName) {
        if (rawName == null)
            return "";

        Collections.sort(rawName);

        StringBuilder res = new StringBuilder();
        for (String part : rawName) {
            res.append(part);
            res.append(", ");
        }
        res.delete(res.length() - 2, res.length());

        return res.toString();
    }

    private Button getDFAButton(NFAutomaton nfAutomaton) {
        Button button = new Button("Преобразовать НКА в ДКА");
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Automaton automaton = (nfAutomaton.transformNFA2DFA());
            TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(automaton);

            Text automatonInfo = new Text("Преобразованный автомат");
            automatonInfo.setFill(Color.WHITESMOKE);
            automatonInfo.setFont(Font.font("System", 20));

            var newPane = getMainPane(automatonTableView, automatonInfo);

            var returnToStartButton = new Button("Вернуться в начало");
            setupButtonAsReturnToStart(returnToStartButton);
            AnchorPane.setTopAnchor(returnToStartButton, 10.0);
            AnchorPane.setRightAnchor(returnToStartButton, 10.0);

            newPane.getChildren().add(returnToStartButton);

            Loader.showStage(new Scene(newPane), true);
        });
        return button;
    }

    private AnchorPane getMainPane(TableView<String[]> automatonTableView, Text automatonInfo) {
        AnchorPane mainPane = new AnchorPane(automatonTableView, automatonInfo);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 25.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setLeftAnchor(automatonInfo, 25.0);
        AnchorPane.setTopAnchor(automatonInfo, 10.0);

        return mainPane;
    }
}
