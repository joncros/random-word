package com.github.joncros.random_word.gui;

import eu.hansolo.fx.spinner.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.CENTER_LEFT;

public class Gui extends Application {
    private static final int NUM_SPINNERS_INITIAL = 10;
    private static final int MAX_WORD_LENGTH = 30;

    private Scene scene;

    private List<CanvasSpinner> spinners;

    private Button generateButton;
    private EventHandler<ActionEvent> buttonEventHandler;

    private Label sourceLabel = new Label("Word Source");
    private ChoiceBox<Object> sourceChoiceBox;
    private ObjectProperty<Object> choiceBoxValueProperty;
    private final Object DatamuseChoice = new Object();
    private final Object TextfileChoice = new Object();

    private Label minLabel= new Label("Min Length");
    private Spinner<Integer> minLengthSpinner;
    private Label maxLabel = new Label("Max Length");
    private Spinner<Integer> maxLengthSpinner;

    @Override
    public void init() {
        //create NUM_SPINNERS_INITIAL spinnners
        spinners = new ArrayList<>();
        for (int i = 0; i < NUM_SPINNERS_INITIAL; i++) {
            spinners.add(i, new CanvasSpinner(SpinnerType.ALPHABETIC));
        }

        //create button
        generateButton = new Button("Generate Word");
        //set button action
        //generateButton.setOnAction(buttonEventHandler);

        //create control to choose word source
        sourceChoiceBox = new ChoiceBox<>();
        sourceChoiceBox.getItems().addAll(DatamuseChoice, TextfileChoice);
        sourceChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Object o) {
                if (o == DatamuseChoice) return "DataMuse";
                else if (o == TextfileChoice) return "Text File";
                else {
                    File file = (File) o;
                    return file.getName();
                }
            }

            @Override
            public Object fromString(String s) {
                if (s.equals("DataMuse")) return DatamuseChoice;
                else if (s.equals("Text File")) return TextfileChoice;
                else {
                    File file = new File(s);
                    return file;
                }
            }
        });
        choiceBoxValueProperty = sourceChoiceBox.valueProperty();
        choiceBoxValueProperty.addListener((observableValue, o, n) -> {
            if (n == TextfileChoice) {
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
        HBox spinnerBox = new HBox();
        spinnerBox.getChildren().setAll(spinners);

        HBox controls = new HBox(sourceLabel, sourceChoiceBox, minLabel, minLengthSpinner, maxLabel, maxLengthSpinner);
        controls.setSpacing(10);
        controls.setAlignment(CENTER_LEFT);

        VBox vBox = new VBox(spinnerBox, controls, generateButton);
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
}
