package com.cdarobotics.cdalib.controllers;

/**
 * A no-op {@link Controller} whose {@link #update(double, double)} always returns {@code 0}. Useful
 * as a placeholder or as a disabled term within a {@link CompoundController}.
 */
public class NullController extends Controller {

    /**
     * @param target the initial setpoint (unused, since the output is always {@code 0})
     */
    public NullController(double target) {
        super(target);
    }

    @Override
    public double update(double current, double dt) {
        return 0;
    }
}
