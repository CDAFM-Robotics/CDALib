package com.cdarobotics.cdalib.controllers;

/**
 * A feedforward {@link Controller}. The output is computed purely from the current measurement as
 * {@code kS + kF * current + cos(kCos * current)}, combining a static term, a term proportional to
 * the measurement, and a cosine term (e.g. to compensate for gravity on a rotating arm).
 */
public class FFController extends Controller {

    private final double kS;
    private final double kF;
    private final double kCos;


    /**
     * @param target the initial setpoint
     * @param kS     the static feedforward term
     * @param kF     the gain applied proportionally to the current measurement
     * @param kCos   the scale applied to the measurement inside the cosine term
     */
    public FFController(double target, double kS, double kF, double kCos) {
        super(target);

        this.kS = kS;
        this.kF = kF;
        this.kCos = kCos;
    }

    @Override
    public double update(double current, double dt) {
        return kS + kF * current + Math.cos(kCos * current);
    }
}
