package com.cdarobotics.cdalib.controllers;


public class PIDController extends Controller {

    private final double kP;
    private final double kI;
    private final double kD;

    private double previousError;
    private double integral = 0;


    public PIDController(double target, double kP, double kI, double kD) {
        super(target);

        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        previousError = target;
    }

    @Override
    public double update(double current, double dt) {

        double error = target - current;

        integral += (previousError + error) / 2 * dt;

        double derivative = (error - previousError) / dt;

        previousError = error;

        return error * kP + integral * kI + derivative * kD;
    }
}
