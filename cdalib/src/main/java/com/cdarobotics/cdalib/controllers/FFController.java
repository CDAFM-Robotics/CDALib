package com.cdarobotics.cdalib.controllers;

public class FFController extends Controller {

    private final double kS;
    private final double kF;
    private final double kCos;


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
