package org.openjfx.Controllers;

import algorithms.Adduction;
import algorithms.Isomorphism;
import automaton.Automaton;
import automaton.IsomorphismResult;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Arrays;

import static org.openjfx.Controllers.AutomatonInputController.setupButtonAsReturnToStart;
import static org.openjfx.Controllers.Controller.automatonList;
import static org.openjfx.Controllers.TaskOneController.setupMainPaneForAutomatonsTableViewDisplay;

public class TaskTwoController {

    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton tableBasedAutomaton = automatonList.get(0);
        Automaton regexBasedAutomaton = automatonList.get(1);
        TableView<String[]> firstAutomatonTableView = TaskOneController.createAutomatonJumpTableTableView(tableBasedAutomaton);
        TableView<String[]> secondAutomatonTableView = TaskOneController.createAutomatonJumpTableTableView(regexBasedAutomaton);

        Text firstAutomatonInfo = new Text("Автомат, построенный по таблице");
        firstAutomatonInfo.setFill(Color.WHITESMOKE);
        firstAutomatonInfo.setFont(Font.font("System", 20));

        Text secondAutomatonInfo = new Text("Автомат, построенный по регулярному выражению");
        secondAutomatonInfo.setFill(Color.WHITESMOKE);
        secondAutomatonInfo.setFont(Font.font("System", 20));

        Button finalizeAutomatonsButton = getFinalizeAutomatonsButton(tableBasedAutomaton, regexBasedAutomaton);

        setupMainPaneForAutomatonsTableViewDisplay(mainPane, firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo, secondAutomatonInfo, finalizeAutomatonsButton);
    }

    private Button getFinalizeAutomatonsButton(Automaton tableBasedAutomaton, Automaton regexBasedAutomaton) {
        Button button = new Button("Привести ДКА");
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Automaton adductedTableBasedAutomaton;
            Automaton adductedRegexBasedAutomaton;
            try {
                 adductedTableBasedAutomaton = Adduction.buildAdductedAutomat(tableBasedAutomaton);
                 adductedRegexBasedAutomaton = Adduction.buildAdductedAutomat(regexBasedAutomaton);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return;
            }
            TableView<String[]> firstAutomatonTableView = TaskOneController.createAutomatonJumpTableTableView(adductedTableBasedAutomaton);
            TableView<String[]> secondAutomatonTableView = TaskOneController.createAutomatonJumpTableTableView(adductedRegexBasedAutomaton);

            Text firstAutomatonInfo = new Text("Приведенный автомат, построенный по таблице");
            firstAutomatonInfo.setFill(Color.WHITESMOKE);
            firstAutomatonInfo.setFont(Font.font("System", 20));

            Text secondAutomatonInfo = new Text("Приведенный автомат, построенный по регулярному выражению");
            secondAutomatonInfo.setFill(Color.WHITESMOKE);
            secondAutomatonInfo.setFont(Font.font("System", 20));

            Button checkIfIsomorphicButton = getCheckIfIsomorphicButton(adductedTableBasedAutomaton, adductedRegexBasedAutomaton);

            var newPane = getMainPane(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo, secondAutomatonInfo, checkIfIsomorphicButton);

            Loader.showStage(new Scene(newPane), true);
        });
        return button;
    }

    private AnchorPane getMainPane(TableView<String[]> firstAutomatonTableView, TableView<String[]> secondAutomatonTableView, Text firstAutomatonInfo, Text secondAutomatonInfo, Button checkIfIsomorphicButton) {
        AnchorPane mainPane = new AnchorPane(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo, secondAutomatonInfo, checkIfIsomorphicButton);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setRightAnchor(secondAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(secondAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonInfo, 10.0);

        AnchorPane.setBottomAnchor(checkIfIsomorphicButton, 10.0);
        AnchorPane.setRightAnchor(checkIfIsomorphicButton, 10.0);

        return mainPane;
    }

    private Button getCheckIfIsomorphicButton(Automaton first, Automaton second) {
        Button checkIfIsomorphicButton = new Button("Проверить автоматы на изоморфность");
        checkIfIsomorphicButton.setOnAction(event -> {
            IsomorphismResult result;
            try {
                result = Isomorphism.automatsAreIsomorphic(first, second);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return;
            }
            AnchorPane newPane = new AnchorPane();
            newPane.setStyle("-fx-background-color: #2e3348;");
            if (result.areIsomorphic) {
                Text text = new Text("Автоматы изоморфны");
                text.setFont(Font.font("System", FontWeight.BOLD, 20));
                text.setFill(Color.WHITESMOKE);
                Label label = new Label("Биекция-изоморфизм");
                label.setFont(Font.font("System", FontPosture.ITALIC, 16));
                label.setTextFill(Color.WHITESMOKE);
                TableView<String[]> tableView = getIsomorphismTable(result);
                newPane.getChildren().addAll(text, tableView, label);

                AnchorPane.setTopAnchor(text, 10.0);
                AnchorPane.setLeftAnchor(text, 10.0);

                AnchorPane.setTopAnchor(label, 45.0);
                AnchorPane.setLeftAnchor(label, 10.0);

                AnchorPane.setTopAnchor(tableView, 70.0);
                AnchorPane.setLeftAnchor(tableView, 10.0);
            }
            else {
                Text text = new Text("Автоматы неизоморфны");
                text.setFont(Font.font("System", FontWeight.BOLD, 20));
                text.setFill(Color.WHITESMOKE);
                AnchorPane.setLeftAnchor(text, 10.0);
                AnchorPane.setTopAnchor(text, 10.0);
                newPane.getChildren().add(text);
                Text text1 = null;
                if (result.wordIn1ThatNotIn2 != null) {
                    if (result.wordIn1ThatNotIn2.equals(""))
                        text1 = new Text("Слово, принимаемое первым автоматом, но не принимаемое вторым: Пустое слово");
                    else
                        text1 = new Text("Слово, принимаемое первым автоматом, но не принимаемое вторым: '" + result.wordIn1ThatNotIn2 + "'");
                    text1.setFont(Font.font("System", 16));
                    text1.setFill(Color.WHITESMOKE);
                    newPane.getChildren().add(text1);
                    AnchorPane.setLeftAnchor(text1, 10.0);
                    AnchorPane.setTopAnchor(text1, 50.0);
                }
                Text text2;
                if (result.wordIn2ThatNotIn1 != null) {
                    if (result.wordIn2ThatNotIn1.equals(""))
                        text2 = new Text("Слово, принимаемое вторым автоматом, но не принимаемое первым: Пустое слово");
                    else
                        text2 = new Text("Слово, принимаемое вторым автоматом, но не принимаемое первым: '" + result.wordIn2ThatNotIn1 + "'");
                    text2.setFont(Font.font("System", 16));
                    text2.setFill(Color.WHITESMOKE);
                    newPane.getChildren().add(text2);
                    AnchorPane.setLeftAnchor(text2, 10.0);
                    if (text1 == null)
                        AnchorPane.setTopAnchor(text2, 50.0);
                    else
                        AnchorPane.setTopAnchor(text2, 75.0);
                }
            }

            var returnToStartButton = new Button("Вернуться в начало");
            setupButtonAsReturnToStart(returnToStartButton);
            AnchorPane.setTopAnchor(returnToStartButton, 10.0);
            AnchorPane.setRightAnchor(returnToStartButton, 10.0);

            newPane.getChildren().add(returnToStartButton);


            checkIfIsomorphicButton.getScene().getWindow().hide();
            Loader.showStage(new Scene(newPane), true);
        });
        return checkIfIsomorphicButton;
    }

    private TableView<String[]> getIsomorphismTable(IsomorphismResult result) {
        String[][] isomorphismTable = new String[result.associations.size()][2];

        String[] arr = result.associations.keySet().toArray(new String[0]);
        for (int i = 0; i < arr.length; i++) {
            isomorphismTable[i][0] = arr[i];
            isomorphismTable[i][1] = result.associations.get(arr[i]);
        }

        Arrays.sort(isomorphismTable, (arr1, arr2) -> {
            try {
                String replacedArr1 = arr1[0].replaceAll("<-", "").replaceAll("->", "").strip();
                String replacedArr2 = arr2[0].replaceAll("<-", "").replaceAll("->", "").strip();
                return Integer.compare(Integer.parseInt(replacedArr1), Integer.parseInt(replacedArr2));
            }
            catch (NumberFormatException e) {
                return arr1[0].compareTo(arr2[0]);
            }
        });

        ObservableList<String[]> data = FXCollections.observableArrayList(isomorphismTable);
        TableView<String[]> isomorphismTableView = new TableView<>();

        int columnWidth = 150;
        for (int i = 0; i < 2; i++) {
            String name;
            if (i == 0)
                name = "Первый автомат";
            else
                name = "Второй автомат";
            TableColumn<String[], String> tableColumn = new TableColumn<>(name);
            tableColumn.setMinWidth(columnWidth);
            tableColumn.setPrefWidth(columnWidth);
            tableColumn.setMaxWidth(columnWidth);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[columnNumber])));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
            isomorphismTableView.getColumns().add(tableColumn);
        }

        isomorphismTableView.setItems(data);
        isomorphismTableView.setFixedCellSize(25);
        isomorphismTableView.prefHeightProperty().bind(Bindings.size(isomorphismTableView.getItems()).multiply(isomorphismTableView.getFixedCellSize()).add(26));
        isomorphismTableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(isomorphismTableView.getFixedCellSize()).add(26));

        return isomorphismTableView;
    }
}

