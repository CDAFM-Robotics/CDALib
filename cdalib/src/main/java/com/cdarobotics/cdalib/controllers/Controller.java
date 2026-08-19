package com.cdarobotics.cdalib.controllers;

public abstract class Controller {
    protected double target;

    public Controller(double target) {
        this.target = target;
    }

    public abstract double update(double current, double dt);

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }
}
