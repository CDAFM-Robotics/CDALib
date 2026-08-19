package com.cdarobotics.cdalib.subsystems;

import com.cdarobotics.cdalib.bindings.BindingManager;
import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.cdarobotics.cdalib.devices.actuators.MotorDevice;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class PositionMotorSubsystem extends Subsystem {

    private final MotorDevice motor;

    private Controller controller;

    public PositionMotorSubsystem(HardwareMap hardwareMap, String motorName) {
        motor = new MotorDevice(hardwareMap, motorName);
        controller = new NullController(0);
    }

    public PositionMotorSubsystem(HardwareMap hardwareMap, String motorName, Controller controller) {
        motor = new MotorDevice(hardwareMap, motorName);
        this.controller = controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    public void setPosition(double position) {
        controller.setTarget(position);
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
        previousTime = System.nanoTime() / 1_000_000_000.0;
    }

    private double previousTime;

    @Override
    public void update() {
        double time = System.nanoTime();
        motor.setPower(controller.update(motor.getCurrentPosition(), time - previousTime));
        previousTime = time;
    }

    @Override
    public void stop() {

    }
}
