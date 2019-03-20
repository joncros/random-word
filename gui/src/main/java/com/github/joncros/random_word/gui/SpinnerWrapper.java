package com.github.joncros.random_word.gui;

import eu.hansolo.fx.spinner.CanvasSpinner;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Holds a eu.hansolo.fx.spinner.CanvasSpinner and a queue of target values for the spinner to spin to
 */
class SpinnerWrapper {
    private CanvasSpinner spinner;
    private Deque<Double> targetValues;
    private double animationRate;

    /**
     *
     * @param spinner
     * @param animationRate Number of milliseconds it should take Spinner to spin to an adjacent value
     */
    SpinnerWrapper(CanvasSpinner spinner, double animationRate) {
        this.spinner = spinner;
        this.animationRate = animationRate;
        targetValues = new ArrayDeque<>();
    }

    CanvasSpinner getSpinner() {
        return spinner;
    }

    boolean hasTargetValue() {
        return (targetValues.size() > 0);
    }

    Double getNextTargetValue() {
        return targetValues.poll();
    }

    void pushTargetValue(Double value) {
        targetValues.push(value);
    }

    void spinUp() {

    }
}
