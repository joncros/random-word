package com.github.joncros.random_word.gui;

import com.github.joncros.random_word.core.WordService;
import eu.hansolo.fx.spinner.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.File;
import java.security.Key;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static javafx.geometry.Pos.CENTER_LEFT;

/**
 * Refactor: have RandomWord expose an ObservableList<Character> (or equivalent Property). Create
 * RandomWordObserver interface or abstract class?
 */

public class Gui extends Application {
    static final int MAX_WORD_LENGTH = 10;
    static final int NUM_SPINNERS_INITIAL = MAX_WORD_LENGTH;

    // Number of seconds a CanvasSpinnner should take to cycle from 'A' to ' '
    static final int CYCLE_DURATION = 7;

    private Scene scene;

    List<CanvasSpinner> spinners;
    char[] chars;  //holds the target letter for each spinner
    List<Timeline> timelines; //holds the timeline for each spinner

    Button generateButton;
    //final EventHandler<ActionEvent> buttonEventHandler; = actionEvent -> test.animate(this);

    Label sourceLabel = new Label("Word Source");
    ChoiceBox<Object> sourceChoiceBox;
    ObjectProperty<Object> choiceBoxValueProperty;
    enum WORD_SERVICES {
        DATAMUSE,
        TEXTFILE
    }

    Label minLabel= new Label("Min Length");
    Spinner<Integer> minLengthSpinner;
    Label maxLabel = new Label("Max Length");
    Spinner<Integer> maxLengthSpinner;

    @Override
    public void init() {
        //create NUM_SPINNERS_INITIAL spinnners
        spinners = new ArrayList<>();
        chars = new char[NUM_SPINNERS_INITIAL];
        for (int i = 0; i < NUM_SPINNERS_INITIAL; i++) {
            spinners.add(i, new CanvasSpinner(SpinnerType.ALPHABETIC));
        }
        timelines = new ArrayList<>();

//        Display ' ' initially on all spinners
//        for (int i = 0; i < NUM_SPINNERS_INITIAL; i++) {
//            spinners.get(i).setValue(27);
//        }

        //create button
        generateButton = new Button("Generate Word");
        //set button action
        //generateButton.setOnAction(buttonEventHandler);

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
        HBox spinnerBox = new HBox();
        spinnerBox.getChildren().addAll(spinners);

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

    /*
    Converts a char into the int value, in the range 0-27, required to display it on a
    eu.hansolo.fx alphabetic Spinner. Alphabetic spinners have the characters A-Z and space.
    lowercase letters are converted to uppercase, and any non-letter character is converted to
    a space.
    */
    int convert(char c) {
        if (c == ' ') {
            return 27;
        }
        else if (c >= 65 && c <= 90) {  //uppercase letters
            return c - 65;
        }
        else if (c >= 97 && c <= 122) {  //lowercase letters, c - 65 - 32
            return c - 97;
        }
        else {  //character not present on Alphabetic spinners
            return 27;
        }
    }

}
