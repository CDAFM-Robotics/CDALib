package com.cdarobotics.cdalib.controllers;

/**
 * A {@link Controller} that combines several sub-controllers, summing their outputs each loop.
 * Typically used to add a feedforward term to a PID feedback term (e.g.
 * {@link PIDController} plus {@link FFController}).
 *
 * <p>Note that each sub-controller keeps its own {@link #target}; setting the target on this
 * compound controller does not propagate to the sub-controllers.
 */
public class CompoundController extends Controller {

    private Controller[] controllers;

    /**
     * @param target      the initial setpoint of the compound controller itself
     * @param controllers the sub-controllers whose outputs are summed each update
     */
    public CompoundController(double target, Controller... controllers) {
        super(target);

        this.controllers = controllers;
    }

    @Override
    public double update(double current, double dt) {
        double result = 0;

        for (Controller controller : controllers) {
            result += controller.update(current, dt);
        }
        return result;
    }
}
