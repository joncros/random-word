package com.github.joncros.random_word.gui;

import eu.hansolo.fx.spinner.*;
import javafx.application.Application;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.CENTER_LEFT;

public class Gui extends Application {
    private static final int NUM_SPINNERS_INITIAL = 10;
    private static final int MAX_WORD_LENGTH = 30;

    private List<CanvasSpinner> spinners;

    private Button generateButton;
    private EventHandler<ActionEvent> buttonEventHandler;

    private Label sourceLabel = new Label("Word Source");
    private ChoiceBox<String> sourceChoiceBox;

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
        sourceChoiceBox.getItems().addAll("DataMuse", "Text File");

        //create controls to choose word length
        minLengthSpinner = new Spinner<>(0, MAX_WORD_LENGTH, 5);
        minLengthSpinner.setMaxWidth(60);
        minLengthSpinner.setEditable(true);

        maxLengthSpinner = new Spinner<>(2, MAX_WORD_LENGTH, 2);
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

        Scene scene = new Scene(vBox);
        stage.setTitle("Generate Random Words");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
