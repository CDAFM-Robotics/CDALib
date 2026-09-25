package com.cdarobotics.cdalib.controllers;


/**
 * A proportional-integral-derivative (PID) feedback {@link Controller}. The output is the sum of a
 * proportional term on the current error, an integral term accumulated with trapezoidal
 * integration, and a derivative term on the rate of change of error.
 */
public class PIDController extends Controller {

    private final double kP;
    private final double kI;
    private final double kD;

    private double previousError;
    private double integral = 0;


    /**
     * @param target the initial setpoint
     * @param kP     the proportional gain
     * @param kI     the integral gain
     * @param kD     the derivative gain
     */
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
