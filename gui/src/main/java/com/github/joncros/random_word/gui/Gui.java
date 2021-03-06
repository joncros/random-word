package com.github.joncros.random_word.gui;

import com.github.joncros.random_word.core.*;
import eu.hansolo.fx.spinner.CanvasSpinner;
import eu.hansolo.fx.spinner.SpinnerType;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.CENTER_LEFT;

public class Gui extends Application {
    private static final int MAX_WORD_LENGTH = 10;
    private static final int NUM_SPINNERS_INITIAL = MAX_WORD_LENGTH;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private Scene scene;
    private Controller controller;

    private List<CanvasSpinner> spinners;

    private Button generateButton;

    private Label sourceLabel = new Label("Word Source");
    private ChoiceBox<Object> sourceChoiceBox;
    private ObjectProperty<Object> choiceBoxValueProperty;
    private enum WORD_SERVICES {
        DATAMUSE,
        TEXTFILE
    }

    private Label minLabel= new Label("Min Length");
    private Spinner<Integer> minLengthSpinner;
    private Label maxLabel = new Label("Max Length");
    private Spinner<Integer> maxLengthSpinner;

    private Label wordLabel = new Label("Generated Word");
    private TextField wordField = new TextField();

    @Override
    public void init() {
        //create NUM_SPINNERS_INITIAL spinnners
        spinners = new ArrayList<>();
        for (int i = 0; i < NUM_SPINNERS_INITIAL; i++) {
            spinners.add(i, new CanvasSpinner(SpinnerType.ALPHABETIC));
        }

        generateButton = new Button("Generate Word");
        generateButton.setOnAction(actionEvent -> generate());

        //create control to choose word source
        sourceChoiceBox = new ChoiceBox<>();
        sourceChoiceBox.getItems().addAll(WORD_SERVICES.values());
        sourceChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Object o) {
                if (o == WORD_SERVICES.DATAMUSE) {
                    return "DataMuse";
                } else if (o == WORD_SERVICES.TEXTFILE) {
                    return "Text File";
                } else {
                    File file = (File) o;
                    return file.getName();
                }
            }

            @Override
            public Object fromString(String s) {
                if (s.equals("DataMuse")) {
                    return WORD_SERVICES.DATAMUSE;
                } else {
                    if (s.equals("Text File")) {
                        return WORD_SERVICES.TEXTFILE;
                    } else {
                        return new File(s);
                    }
                }
            }
        });
        choiceBoxValueProperty = sourceChoiceBox.valueProperty();
        choiceBoxValueProperty.addListener((observableValue, o, n) -> {
            if (n == WORD_SERVICES.TEXTFILE) {
                choiceBoxTextFileChosen();
            }
        });

        //create controls to choose word length
        minLengthSpinner = new Spinner<>(0, MAX_WORD_LENGTH, 5);
        minLengthSpinner.setMaxWidth(60);
        minLengthSpinner.setEditable(true);

        maxLengthSpinner = new Spinner<>(2, MAX_WORD_LENGTH, 10);
        maxLengthSpinner.setMaxWidth(60);
        maxLengthSpinner.setEditable(true);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Gui::errorDialogue);

        HBox spinnerBox = new HBox();
        spinnerBox.getChildren().addAll(spinners);

        HBox controls = new HBox(sourceLabel, sourceChoiceBox, minLabel, minLengthSpinner, maxLabel, maxLengthSpinner);
        controls.setSpacing(10);
        controls.setAlignment(CENTER_LEFT);

        HBox controls2 = new HBox(generateButton, wordLabel, wordField);
        controls2.setSpacing(10);
        controls2.setAlignment(CENTER_LEFT);

        VBox vBox = new VBox(spinnerBox, controls, controls2);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        spinnerBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(vBox);
        stage.setTitle("Generate Random Words");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
    Fired when the value "Text File" is chosen in the choice box.
     */
    private File choiceBoxTextFileChosen() {
        //todo call this from buttonEventHandler if choice box value is "Text File?"
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selected = fileChooser.showOpenDialog(scene.getWindow());

        if (selected != null) {
            ObservableList<Object> items = sourceChoiceBox.getItems();
            if ( !items.contains(selected) ) {
                //file has not been added to combo box items
                items.add(selected);
            }
            choiceBoxValueProperty.setValue(selected);
        }

        return selected;
    }

    /**
     * Button event handler for the "Generate Word" button
     */
    private void generate() {
        WordService wordService = null;
        // TODO handle exception by popup
        Object sourceChoice = sourceChoiceBox.getValue();
        if (sourceChoice instanceof File) {
            try {
                wordService = new TextFileWordService((File) sourceChoice);
            } catch (IOException e) {
                printException(Thread.currentThread(), e);
            }
        } else {
            wordService = new DatamuseWordService();
        }
        controller = new Controller(
                new RandomWord(
                        minLengthSpinner.getValue(),
                        maxLengthSpinner.getValue(),
                        wordService,
                        new RandomLetterGenerator(ALPHABET)
                ),
                spinners);
        try {
            String word = controller.generateRandomWord();
            wordField.setText(word);
        } catch (IOException e) {
            errorDialogue(Thread.currentThread(), e);
        }
    }

    private static void printException(Thread t, Throwable e) {
        System.out.print(e.getMessage());
    }

    private static void errorDialogue(Thread t, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (e instanceof IOException) {
            alert.setHeaderText("Error accessing word list");
        }
        alert.setContentText(e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Exception Stacktrace:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setMaxWidth(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane exPane = new GridPane();
        exPane.setMaxWidth(Double.MAX_VALUE);
        exPane.add(label, 0, 0);
        exPane.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(exPane);
        alert.show();
    }

}
