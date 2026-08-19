package com.cdarobotics.cdalib.controllers;

public class NullController extends Controller {

    public NullController(double target) {
        super(target);
    }

    @Override
    public double update(double current, double dt) {
        return 0;
    }
}
