package com.cdarobotics.cdalib.subsystems;

import com.cdarobotics.cdalib.bindings.BindingManager;
import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.cdarobotics.cdalib.devices.actuators.MotorDevice;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link Subsystem} that drives a single motor toward a target position using a
 * {@link Controller}. Each {@link #update()} feeds the motor's current position and the elapsed
 * time to the controller and applies the resulting power, so the motor is continuously driven
 * toward the target set by {@link #setPosition(double)}.
 */
public class PositionMotorSubsystem extends Subsystem {

    private final MotorDevice motor;

    private Controller controller;

    /**
     * Resolves the motor from the hardware map and starts with a {@link NullController} (no motion)
     * until a real controller is set via {@link #setController(Controller)}.
     *
     * @param hardwareMap the OpMode hardware map.
     * @param motorName   the configured name of the motor.
     */
    public PositionMotorSubsystem(HardwareMap hardwareMap, String motorName) {
        motor = new MotorDevice(hardwareMap, motorName);
        controller = new NullController(0);
    }

    /**
     * Resolves the motor from the hardware map and drives it with the given controller.
     *
     * @param hardwareMap the OpMode hardware map.
     * @param motorName   the configured name of the motor.
     * @param controller  the controller that computes motor power from position error.
     */
    public PositionMotorSubsystem(HardwareMap hardwareMap, String motorName, Controller controller) {
        motor = new MotorDevice(hardwareMap, motorName);
        this.controller = controller;
    }

    /** Replaces the controller used to drive the motor toward its target. */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /** Sets the motor's rotation direction. */
    public void setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    /** Sets the target position the controller drives the motor toward. */
    public void setPosition(double position) {
        controller.setTarget(position);
    }

    @Override
    public void init() {

    }

    @Override
    public void init_loop() {
        
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
