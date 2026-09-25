package com.cdarobotics.cdalib.controllers;

/**
 * Base type for closed-loop and feedforward controllers. A controller drives a measured value
 * toward a {@link #target} setpoint; each loop {@link #update(double, double)} is called with the
 * current measurement and elapsed time and returns the control output to apply.
 */
public abstract class Controller {
    /** The setpoint this controller is driving the measured value toward. */
    protected double target;

    /**
     * @param target the initial setpoint
     */
    public Controller(double target) {
        this.target = target;
    }

    /**
     * Computes the control output for this loop.
     *
     * @param current the current measured value
     * @param dt      the time elapsed since the previous update, in seconds
     * @return the control output to apply
     */
    public abstract double update(double current, double dt);

    /** @return the current setpoint. */
    public double getTarget() {
        return target;
    }

    /**
     * Sets the setpoint.
     *
     * @param target the new setpoint
     */
    public void setTarget(double target) {
        this.target = target;
    }
}
