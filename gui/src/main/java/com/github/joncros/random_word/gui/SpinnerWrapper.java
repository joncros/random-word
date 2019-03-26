package com.github.joncros.random_word.gui;

import eu.hansolo.fx.spinner.CanvasSpinner;
import eu.hansolo.fx.spinner.event.SpinnerEvent;
import eu.hansolo.fx.spinner.event.SpinnerObserver;
import javafx.animation.Timeline;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Holds a eu.hansolo.fx.spinner.CanvasSpinner and a queue of target values for the spinner to spin to
 */
class SpinnerWrapper {
    private CanvasSpinner spinner;
    private Queue<Double> targetValues;
    private Double previousTarget;
    private double animationRate;
    private Timeline timeline;
    private SpinnerObserver spinnerObserver;

    enum Status {
        SPINNING,            // Default Status: spinning to the next consecutive spinner value
        PAUSED,              // Has reached current target, pausing before moving to next target
        STOPPED              // Has reached target, does not have any other targets
    }
    private Status currentStatus;

    /**
     *
     * @param spinner A eu.hansolo.fx.spinner.CanvasSpinner
     * @param animationRate Number of milliseconds it should take Spinner to spin to an adjacent value
     */
    SpinnerWrapper(CanvasSpinner spinner, double animationRate) {
        this.spinner = spinner;
        this.animationRate = animationRate;
        this.previousTarget = null;
        targetValues = new ArrayDeque<>();
        this.currentStatus = Status.STOPPED;

        spinnerObserver = (SpinnerEvent evt) -> {
            // if spinner value == target, pause or stop
            // else spin to next value
        };

        this.spinner.setOnValueChanged(spinnerObserver);
    }

    CanvasSpinner getSpinner() {
        return spinner;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    boolean hasTargetValue() {
        return (targetValues.size() > 0);
    }

    Double getNextTargetValue() {
        return targetValues.poll();
    }

    public Double getPreviousTarget() {
        return previousTarget;
    }

    void addTargetValue(Double value) {
        targetValues.add(value);
    }

    /**
     * Start spinning this spinner. No effect unless Status is STOPPED
     */
    void start() {

    }

    /**
     * Continue spinning until ' ' is reached, then stop
     */
    void spinToEnd() {

    }

    /**
     * Spin to the next consecutive value
     */
    private void spinUp() {

    }

    /**
     * For when spinner reaches a target value but there are more targets. Pauses at the value for a
     * set amount of time.
     */
    private void pause() {

    }
}
