package org.openjfx.Controllers;

import automaton.Automaton;
import com.google.common.collect.HashBasedTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.*;

import static org.openjfx.Controllers.Controller.automatonList;

public class AutomatonInputController {

    @FXML
    protected TextField alphabetField;

    @FXML
    protected Button returnToPreviousStateButton;

    @FXML
    protected Button createTableButton;

    @FXML
    protected AnchorPane inputWindowMainPane;

    @FXML
    protected TextField statesCountField;

    protected AnchorPane tableWindowMainPane;

    protected Text inputCorrectnessText;

    @FXML
    protected void initialize() {
        setupButtonAsReturnToPreviousState(returnToPreviousStateButton);
        initCreateTableButton();
        setupStatesCountField();
        setupAlphabetField();
        inputCorrectnessText = new Text();
    }

    @FXML
    public static void setupButtonAsReturnToPreviousState(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxml("/solveTasksMenu.fxml", false);
        });
    }

    public static void setupButtonAsReturnToStart(Button button) {
        button.setOnAction(event -> {
            button.getScene().getWindow().hide();
            automatonList.clear();
            Loader.loadFxmlStartupPage();
        });
    }

    protected void setupStatesCountField() {
        statesCountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                statesCountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    protected void setupAlphabetField() {
        alphabetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-1,]*")) {
                alphabetField.setText(newValue.replaceAll("[^a-zA-Z0-1,]", ""));
            }
        });
    }

    public static Text getColoredText(String text, Color color, Font font) {
        Text text1 = new Text(text);
        text1.setFill(color);
        text1.setFont(font);
        return text1;
    }

    public static TextField getTextFieldWithPrompt(String promptText, int prefWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setEditable(true);
        textField.setPrefWidth(prefWidth);
        return textField;
    }

    protected boolean checkAlphabetAndStatesCorrectness(String[] states, String[] alphabet) {
        inputWindowMainPane.getChildren().remove(inputCorrectnessText);

        if (states.length == 0) {
            inputCorrectnessText = new Text("В автомате не может быть 0 состояний");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        if (alphabetField.getText().equals("") || (alphabetField.getText().trim().length() == 0)  || containsDuplicates(alphabet)) {
            if (containsDuplicates(alphabet))
                inputCorrectnessText = new Text("Алфавит не может содержать повторяющиеся буквы");
            else
                inputCorrectnessText = new Text("Алфавит не может быть пустым");
            inputCorrectnessText.setFill(Color.RED);
            inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
            AnchorPane.setBottomAnchor(inputCorrectnessText, 10.0);
            AnchorPane.setLeftAnchor(inputCorrectnessText, 10.0);
            inputWindowMainPane.getChildren().add(inputCorrectnessText);
            return false;
        }

        return true;
    }

    protected boolean checkInputCorrectness(String startVertex, String[] finalVertices, String[] states, TableView<String[]> automatonTableView) {
        if (startVertex.equals("") || finalVertices.length == 0 || finalVertices.length == 1 && Objects.equals(finalVertices[0], "")) {
            setupInputCorrectnessText(inputCorrectnessText, "Неправильно заданы параметры начальной и конечной вершины", automatonTableView);
            return false;
        }

        List<String> statesAsList = Arrays.asList(states);
        if (!statesAsList.contains(startVertex)) {
            setupInputCorrectnessText(inputCorrectnessText, "Автомат не содержит вершины '" + startVertex + "'", automatonTableView);
            return false;
        }

        for (String finalVertex : finalVertices) {
            if (!statesAsList.contains(finalVertex)) {
                setupInputCorrectnessText(inputCorrectnessText, "Автомат не содержит вершины '" + finalVertex + "'", automatonTableView);
                return false;
            }
        }

        if (containsDuplicates(finalVertices)) {
            setupInputCorrectnessText(inputCorrectnessText, "В списке конечных вершин некоторые вершины встречаются больше одного раза", automatonTableView);
            return false;
        }

        ObservableList<String[]> items = automatonTableView.getItems();

        int count = 0;
        for (String[] arr : items) {
            for (String item : arr) {
                if (!item.equals("")) {
                    count++;
                }
            }
        }

        if (count <= items.size()) {
            setupInputCorrectnessText(inputCorrectnessText, "Таблица переходов автомата не заполнена либо заполнена некорректно", automatonTableView);
            return false;
        }

        return true;
    }

    protected void setupInputCorrectnessText(Text inputCorrectnessText, String text, TableView<String[]> tableView) {
        inputCorrectnessText.setText(text);
        inputCorrectnessText.setFill(Color.RED);
        inputCorrectnessText.setFont(Font.font("System", FontPosture.ITALIC, 14));
        AnchorPane.setTopAnchor(inputCorrectnessText, 110.0);
        AnchorPane.setLeftAnchor(inputCorrectnessText, Math.min(tableView.getPrefWidth(), tableView.getMaxWidth()) + 23.0);
    }

    public static boolean containsDuplicates(String[] array)
    {
        Set<String> set = new HashSet<>();
        for (String item : array)
        {
            if (set.contains(item)) return true;
            set.add(item);
        }
        return false;
    }

    protected void initCreateTableButton() {
        createTableButton.setOnAction(event -> {
            try {
                String[] states = new String[Integer.parseInt(statesCountField.getText())];
                String[] alphabet = alphabetField.getText().split(",");

                for (int i = 0; i < states.length; i++) {
                    states[i] = Integer.toString(i + 1);
                }

                if (!checkAlphabetAndStatesCorrectness(states, alphabet))
                    return;

                returnToPreviousStateButton.getScene().getWindow().hide();

                String[][] jumpTable = new String[states.length][alphabet.length + 1];

                for (int i = 0; i < states.length; i++) {
                    for (int j = 0; j < alphabet.length + 1; j++) {
                        if (j == 0)
                            jumpTable[i][j] = Integer.toString(i + 1);
                        else {
                            jumpTable[i][j] = "";
                        }
                    }
                }

                ObservableList<String[]> data = FXCollections.observableArrayList(jumpTable);

                TableView<String[]> automatonTableView = getAutomatonTableView(data, alphabet, states);
                TextField startVertexTextField = getTextFieldWithPrompt("Введите начальное состояние", 325);
                TextField finalVerticesTextField = getTextFieldWithPrompt("Введите через запятую конечные состояния автомата", 325);
                Text automatonInfoText = automatonList.size() == 0
                        ? getColoredText("Введите таблицу переходов первого автомата", Color.WHITESMOKE, Font.font("System", 20))
                        : getColoredText("Введите таблицу переходов второго автомата", Color.WHITESMOKE, Font.font("System", 20));
                Button createAutomatonButton = getCreateAutomatonButton(
                        startVertexTextField,
                        finalVerticesTextField,
                        automatonTableView,
                        states,
                        alphabet
                );
                Button returnToStartButton = new Button("Вернуться в начало");
                setupButtonAsReturnToStart(returnToStartButton);
                tableWindowMainPane = setupMainPaneForTableCreationPage(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText, returnToStartButton);
                Loader.showStage(new Scene(tableWindowMainPane), true);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    public static TableView<String[]> getAutomatonTableView(ObservableList<String[]> tableData, String[] alphabet, String[] states) {
        TableView<String[]> automatonTableView = new TableView<>();
        automatonTableView.setEditable(true);

        int stateColumnWidth = 150;
        int regularColumnWidth = 75;
        int width;


        for (int i = 0; i < alphabet.length + 1; i++) {
            TableColumn<String[], String> tableColumn;
            if (i == 0) {
                tableColumn = new TableColumn<>("Состояние \\ Буква");
                width = stateColumnWidth;
            } else {
                tableColumn = new TableColumn<>(alphabet[i - 1].strip());
                width = regularColumnWidth;
            }
            tableColumn.setMinWidth(width);
            tableColumn.setPrefWidth(width);
            tableColumn.setMaxWidth(width);
            automatonTableView.getColumns().add(tableColumn);
            final int columnNumber = i;
            tableColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue()[columnNumber]));
            tableColumn.setStyle("-fx-alignment: CENTER;");
            tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<String[], String> cell) -> {
                        int row = cell.getTablePosition().getRow();
                        int column = cell.getTablePosition().getColumn();
                        String newValue = cell.getNewValue();
                        if (Arrays.asList(states).contains(newValue) || newValue.isEmpty())
                            tableData.get(row)[column] = newValue;
                        else if (column == 0) {
                            states[row] = newValue;
                            tableData.get(row)[column] = newValue;
                        }
                        automatonTableView.refresh();
                    }
            );
        }

        automatonTableView.setItems(tableData);
        automatonTableView.setFixedCellSize(26);
        automatonTableView.prefHeightProperty().bind(Bindings.size(automatonTableView.getItems()).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.maxHeightProperty().bind((new SimpleIntegerProperty(20)).multiply(automatonTableView.getFixedCellSize()).add(26));
        automatonTableView.prefWidthProperty().bind(new SimpleIntegerProperty(2 + stateColumnWidth + regularColumnWidth * alphabet.length));
        automatonTableView.maxWidthProperty().bind(new SimpleIntegerProperty(stateColumnWidth + regularColumnWidth * 10));
        return automatonTableView;
    }

    protected Button getCreateAutomatonButton(TextField startVertexTextField, TextField finalVerticesTextField, TableView<String[]> automatonTableView, String[] states, String[] alphabet) {
        Button button;
        if (automatonList.size() == 0)
            button = new Button("Создать автомат");
        else
            button = new Button("Привести ДКА");
        button.setOnAction(event2 -> {
            String[][] automatonJumpTable = new String[automatonTableView.getItems().size()][automatonTableView.getColumns().size()];
            ObservableList<String[]> items = automatonTableView.getItems();
            String startVertex = startVertexTextField.getText().strip();
            String[] finalVertices = finalVerticesTextField.getText().split(",");

            for (int i = 0; i < states.length; i++) {
                states[i] = items.get(i)[0];
            }

            tableWindowMainPane.getChildren().remove(inputCorrectnessText);
            if (!checkInputCorrectness(startVertex, finalVertices, states, automatonTableView)) {
                tableWindowMainPane.getChildren().add(inputCorrectnessText);
                tableWindowMainPane.requestLayout();
                return;
            }

            for (int i = 0; i < items.size(); i++) {
                System.arraycopy(items.get(i), 0, automatonJumpTable[i], 0, automatonTableView.getColumns().size());
            }

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 0; j < automatonJumpTable[i].length; j++) {
                    automatonJumpTable[i][j] = automatonJumpTable[i][j].strip();
                }
            }

            for (int i = 0; i < finalVertices.length; i++) {
                finalVertices[i] = finalVertices[i].strip();
            }

            HashBasedTable<String, String, String> jumpTable = HashBasedTable.create();

            for (int i = 0; i < automatonJumpTable.length; i++) {
                for (int j = 1; j < automatonJumpTable[i].length; j++) {
                    jumpTable.put(states[i], alphabet[j - 1], automatonJumpTable[i][j]);
                }
            }

            automatonList.add(new Automaton(false, jumpTable, startVertex, Arrays.asList(finalVertices)));

            button.getScene().getWindow().hide();

            if (automatonList.size() < 2)
                Loader.loadFxml("/automatonInput.fxml", false);
            else
                Loader.loadFxml("/taskOne.fxml", true);
        });
        return button;
    }

    protected AnchorPane setupMainPaneForTableCreationPage(TableView<String[]> automatonTableView,
                                                           Button createAutomatonButton,
                                                           TextField startVertexTextField,
                                                           TextField finalVerticesTextField,
                                                           Text automatonInfoText,
                                                           Button returnToStartButton) {
        AnchorPane mainPane = new AnchorPane(automatonTableView, createAutomatonButton, startVertexTextField, finalVerticesTextField, automatonInfoText, returnToStartButton);
        mainPane.setStyle("-fx-background-color: #2e3348;");

        AnchorPane.setLeftAnchor(automatonTableView, 10.0);
        AnchorPane.setTopAnchor(automatonTableView, 50.0);

        AnchorPane.setTopAnchor(startVertexTextField, 50.0);
        AnchorPane.setLeftAnchor(startVertexTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setTopAnchor(finalVerticesTextField, 80.0);
        AnchorPane.setLeftAnchor(finalVerticesTextField, Math.min(automatonTableView.getPrefWidth(), automatonTableView.getMaxWidth()) + 20.0);

        AnchorPane.setBottomAnchor(createAutomatonButton, 10.0);
        AnchorPane.setRightAnchor(createAutomatonButton, 10.0);

        AnchorPane.setTopAnchor(automatonInfoText, 10.0);
        AnchorPane.setLeftAnchor(automatonInfoText, 10.0);

        AnchorPane.setTopAnchor(returnToStartButton, 10.0);
        AnchorPane.setRightAnchor(returnToStartButton, 10.0);

        return mainPane;
    }
}

