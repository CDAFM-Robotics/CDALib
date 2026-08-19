package com.cdarobotics.cdalib.controllers;

public class CompoundController extends Controller {

    private Controller[] controllers;

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
