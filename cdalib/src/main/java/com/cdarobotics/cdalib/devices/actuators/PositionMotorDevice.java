package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link MotorDevice} that closes a position loop in software, as an alternative to the Lynx
 * firmware's {@code RUN_TO_POSITION}. It is driven by a {@link Controller}: every {@link #update()}
 * measures the encoder position (ticks), runs the controller to produce a motor power, and flushes
 * that power through the inherited write caching.
 *
 * <p>Because {@link #update()} steps a discrete control loop — computing the elapsed {@code dt} and
 * advancing the controller once — it must be called every loop at a steady cadence. Held inside a
 * {@link com.cdarobotics.cdalib.subsystems.DeviceSubsystem} this happens automatically.
 */
public class PositionMotorDevice extends MotorDevice {

    private int position;

    private Controller controller;

    private double lastTime;

    /** Wraps an already-resolved motor and drives it to {@code controller}'s target. */
    public PositionMotorDevice(DcMotorEx motor, Controller controller) {
        super(motor);
        this.controller = controller;
        position = (int) controller.getTarget();
    }

    /** Resolves the motor from the hardware map by configured name, with the given controller. */
    public PositionMotorDevice(HardwareMap hardwareMap, String name, Controller controller) {
        super(hardwareMap, name);
        this.controller = controller;
        position = (int) controller.getTarget();
    }

    /**
     * Wraps an already-resolved motor with no active control law — a {@link NullController} holding
     * {@code 0} — until one is supplied via {@link #setController(Controller)}.
     */
    public PositionMotorDevice(DcMotorEx motor) {
        super(motor);
        this.controller = new NullController(0);
    }

    /**
     * Resolves the motor from the hardware map by configured name with no active control law — a
     * {@link NullController} holding {@code 0} — until one is supplied via
     * {@link #setController(Controller)}.
     */
    public PositionMotorDevice(HardwareMap hardwareMap, String name) {
        super(hardwareMap, name);
        this.controller = new NullController(0);
    }

    /** Swaps the control law; the target position is re-read from the new controller. */
    public void setController(Controller controller) {
        this.controller = controller;
        position = (int) this.controller.getTarget();
    }

    /**
     * Sets the target encoder position, in ticks. Applied via the controller on the next
     * {@link #update()}.
     */
    public void setPosition(int position) {
        this.position = position;
        controller.setTarget(position);
    }

    @Override
    public void update() {

        double currentTime = System.nanoTime() / 1000000000.0;
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        this.setPower(controller.update(this.getCurrentPosition(), dt));

        super.update();
    }
}
