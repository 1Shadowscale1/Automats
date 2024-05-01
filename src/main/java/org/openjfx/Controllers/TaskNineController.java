package org.openjfx.Controllers;

import static org.openjfx.Controllers.Controller.automatonList;

import algorithms.Adduction;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import automaton.Automaton;

public class TaskNineController {
    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton automaton = automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(automaton);

        Automaton prefixAutomaton = automatonList.get(1);
        TableView<String[]> prefixAutomatonTableView = TaskOneController
                .createAutomatonJumpTableTableView(prefixAutomaton);

        Text automatonInfo = new Text("Исходный автомат");
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Text prefixAutomatonInfo = new Text("Префиксное замыкание исходного автомата");
        prefixAutomatonInfo.setFill(Color.WHITESMOKE);
        prefixAutomatonInfo.setFont(Font.font("System", 20));

        var returnToStartButton = new Button("Вернуться в начало");
        setupButtonAsReturnToStart(returnToStartButton);

        Button finalizeAutomatonsButton = getFinalizeAutomatonsButton(prefixAutomaton);

        setupMainPaneForAutomatonsTableViewDisplay(mainPane, automatonTableView, prefixAutomatonTableView,
                automatonInfo, prefixAutomatonInfo, returnToStartButton);

        mainPane.getChildren().add(finalizeAutomatonsButton);
        AnchorPane.setRightAnchor(finalizeAutomatonsButton, 10.0);
        AnchorPane.setBottomAnchor(finalizeAutomatonsButton, 10.0);
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
            Text firstAutomatonInfo, Text secondAutomatonInfo, Button button) {
        mainPane.getChildren().addAll(firstAutomatonTableView, secondAutomatonTableView, firstAutomatonInfo,
                secondAutomatonInfo, button);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(firstAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonTableView, 50.0);

        AnchorPane.setRightAnchor(secondAutomatonTableView, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonTableView, 50.0);

        AnchorPane.setLeftAnchor(firstAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(firstAutomatonInfo, 10.0);

        AnchorPane.setRightAnchor(secondAutomatonInfo, 25.0);
        AnchorPane.setTopAnchor(secondAutomatonInfo, 10.0);

        AnchorPane.setBottomAnchor(button, 10.0);
        AnchorPane.setLeftAnchor(button, 10.0);
    }

    private Button getFinalizeAutomatonsButton(Automaton automaton) {
        Button button = new Button("Получить минимальный ДКА");
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Automaton adductedAutomaton;
            try {
                adductedAutomaton = Adduction.buildAdductedAutomatFromNotFullDFA(automaton);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return;
            }
            TableView<String[]> automatonTableView = TaskOneController
                    .createAutomatonJumpTableTableView(adductedAutomaton);

            Text automatonInfo = new Text("Минимальный автомат");
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
