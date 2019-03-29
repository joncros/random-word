package com.github.joncros.random_word.gui;

import eu.hansolo.fx.spinner.CanvasSpinner;
import eu.hansolo.fx.spinner.event.SpinnerEvent;
import eu.hansolo.fx.spinner.event.SpinnerObserver;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Holds a eu.hansolo.fx.spinner.CanvasSpinner and a queue of target values for the spinner to spin to
 */
class SpinnerWrapper {
    private CanvasSpinner spinner;
    private int index;                      // position of the spinner (ie 0 for first letter's spinner)
    private Queue<Double> targetValues;
    private Double previousTarget;
    private double animationRate;           // Time in milliseconds spinner should take to reach next value
    private final int pauseMultiplier = 6;  // Pause duration is animationRate * pauseMultiplier
    private Timeline timeline;
    private Controller controller;          // Reference to Controller instance, used to call onSpinnerTargetReached

    private SpinnerObserver valueChangedObserver = new SpinnerObserver() {
        @Override
        public void onSpinnerEvent(SpinnerEvent evt) {
            if (currentStatus == Status.SPINNING_TO_TARGET && spinner.getValue() == targetValues.peek()) {
                // spinner reached current target
                targetValues.remove();
                controller.onSpinnerTargetReached(SpinnerWrapper.this);
            } else {
                spinUp();
            }
        }
    };

    enum Status {
        SPINNING,            // Default Status: spinning continuously
        SPINNING_TO_TARGET,  // Spinning until the next target value is reached
        PAUSED,              // Has reached current target, pausing before moving to next target
        STOPPED              // Has reached target, does not have any other targets
    }
    private Status currentStatus;

    /**
     *
     * @param spinner A eu.hansolo.fx.spinner.CanvasSpinner
     * @param animationRate Number of milliseconds it should take Spinner to spin to an adjacent value
     */
    SpinnerWrapper(Controller controller, CanvasSpinner spinner, int index, double animationRate) {
        this.controller = controller;
        this.spinner = spinner;
        this.index = index;
        this.timeline = new Timeline();
        this.animationRate = animationRate;
        this.previousTarget = null;
        this.targetValues = new ArrayDeque<>();
        this.currentStatus = Status.STOPPED;
        this.spinner.setOnValueChanged(valueChangedObserver);
    }

    CanvasSpinner getSpinner() {
        return spinner;
    }

    Status getStatus() {
        return currentStatus;
    }

    boolean hasTargetValue() {
        return (targetValues.size() > 0);
    }

    Double getNextTargetValue() {
        return targetValues.poll();
    }

    Double getPreviousTarget() {
        return previousTarget;
    }

    void addTargetValue(Double value) {
        targetValues.add(value);
    }

    /**
     * Start spinning this spinner. No effect unless Status is STOPPED
     */
    void start() {
        if (currentStatus == Status.STOPPED) {
            currentStatus = Status.SPINNING;
            spinUp();
        }
    }

    /**
     * Spin until the next target value is reached
     */
    void spinToTarget() {
        currentStatus = Status.SPINNING_TO_TARGET;
    }

    /**
     * Continue spinning until ' ' is reached, then stop
     */
    void spinToEnd() {
        spinner.removeSpinnerObserver(valueChangedObserver);
        Double currentValue = spinner.getValue();
        Double targetValue = Controller.convert(' ');
        double difference = targetValue - currentValue;
        KeyValue kv0 = new KeyValue(spinner.valueProperty(), currentValue, Interpolator.LINEAR);
        KeyValue kv1 = new KeyValue(spinner.valueProperty(), targetValue, Interpolator.LINEAR);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(animationRate*difference), kv1);
        timeline.stop();
        timeline.getKeyFrames().setAll(kf0, kf1);
        timeline.play();
    }

    /**
     * Spin to the next consecutive value
     */
    private void spinUp() {
        double targetValue;
        if (Double.compare(spinner.getValue(), spinner.getSpinnerType().getUpperLimit()) == 0) {
            spinner.setValue(0);
            return;
        } else {
            targetValue = spinner.getValue() + 1;
        }
        KeyValue kv0 = new KeyValue(spinner.valueProperty(), spinner.getValue(), Interpolator.LINEAR);
        KeyValue kv1 = new KeyValue(spinner.valueProperty(), targetValue, Interpolator.LINEAR);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(animationRate), kv1);
        timeline.stop();
        timeline.getKeyFrames().setAll(kf0, kf1);
        timeline.play();
    }

    /**
     * For when spinner reaches a target value but there are more targets. Pauses at the value for a
     * set amount of time.
     */
    void pause() {
        currentStatus = Status.PAUSED;
        KeyValue kv0 = new KeyValue(spinner.valueProperty(), spinner.getValue(), Interpolator.LINEAR);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(Duration.millis(animationRate * pauseMultiplier), kv0);
        timeline.stop();
        timeline.getKeyFrames().setAll(kf0, kf1);
        timeline.setOnFinished(actionEvent -> {
            currentStatus = Status.SPINNING_TO_TARGET;
            spinUp();
        });
        timeline.play();
    }

    void stop() {
        currentStatus = Status.STOPPED;
        timeline.stop();
    }
}
