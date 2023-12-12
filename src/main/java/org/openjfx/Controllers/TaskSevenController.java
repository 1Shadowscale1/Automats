package org.openjfx.Controllers;

import algorithms.Adduction;
import automaton.Automaton;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static org.openjfx.Controllers.Controller.automatonList;

public class TaskSevenController {
    @FXML
    private AnchorPane mainPane;

    @FXML
    void initialize() {
        Automaton regexBasedAutomaton = automatonList.get(0);
        TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(regexBasedAutomaton);

        Text automatonInfo = new Text("Автомат, построенный по регулярному выражению");
        automatonInfo.setFill(Color.WHITESMOKE);
        automatonInfo.setFont(Font.font("System", 20));

        Button finalizeAutomatonsButton = getFinalizeAutomatonsButton(regexBasedAutomaton);

        setupMainPaneForAutomatonsTableViewDisplay(mainPane, automatonTableView, automatonInfo, finalizeAutomatonsButton);
    }

    public static void setupButtonAsReturnToStart(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxmlStartupPage();
        });
    }

    private Button getFinalizeAutomatonsButton(Automaton regexBasedAutomaton) {
        Button button = new Button("Привести ДКА");
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            Automaton adductedRegexBasedAutomaton;
            try {
                 adductedRegexBasedAutomaton = Adduction.buildAdductedAutomat(regexBasedAutomaton);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return;
            }
            TableView<String[]> automatonTableView = TaskOneController.createAutomatonJumpTableTableView(adductedRegexBasedAutomaton);

            Text automatonInfo = new Text("Приведенный автомат, построенный по регулярному выражению");
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

    public static void setupMainPaneForAutomatonsTableViewDisplay(AnchorPane mainPane, TableView<String[]> automatonTableView, Text automatonInfo, Button button) {
        mainPane.getChildren().addAll(automatonTableView, automatonInfo, button);

        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 25.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setLeftAnchor(automatonInfo, 25.0);
        AnchorPane.setTopAnchor(automatonInfo, 10.0);

        AnchorPane.setBottomAnchor(button, 10.0);
        AnchorPane.setRightAnchor(button, 10.0);
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
