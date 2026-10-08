package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.controllers.Controller;
import com.cdarobotics.cdalib.controllers.NullController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link MotorDevice} that closes a velocity loop in software, driven by a {@link Controller}.
 * Every {@link #update()} measures the current velocity (ticks per second), runs the controller to
 * produce a motor power, and flushes that power through the inherited write caching. Useful for
 * flywheels and intakes where you want to hold a speed rather than command a fixed open-loop power.
 *
 * <p>Velocity loops usually pair with an {@link com.cdarobotics.cdalib.controllers.FFController} (or
 * a {@link com.cdarobotics.cdalib.controllers.CompoundController} of PID plus feedforward) rather
 * than pure PID.
 *
 * <p>Because {@link #update()} steps a discrete control loop — computing the elapsed {@code dt} and
 * advancing the controller once — it must be called every loop at a steady cadence. Held inside a
 * {@link com.cdarobotics.cdalib.subsystems.DeviceSubsystem} this happens automatically.
 */
public class VelocityMotorDevice extends MotorDevice {

    private Controller controller;

    private double velocity;

    private double lastTime;

    /** Wraps an already-resolved motor and drives it to {@code controller}'s target velocity. */
    public VelocityMotorDevice(DcMotorEx motor, Controller controller) {
        super(motor);
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    /** Resolves the motor from the hardware map by configured name, with the given controller. */
    public VelocityMotorDevice(HardwareMap hardwareMap, String name, Controller controller) {
        super(hardwareMap, name);
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    /**
     * Wraps an already-resolved motor with no active control law — a {@link NullController} holding
     * {@code 0} — until one is supplied via {@link #setController(Controller)}.
     */
    public VelocityMotorDevice(DcMotorEx motor) {
        super(motor);
        this.controller = new NullController(0);
        velocity = 0;
    }

    /**
     * Resolves the motor from the hardware map by configured name with no active control law — a
     * {@link NullController} holding {@code 0} — until one is supplied via
     * {@link #setController(Controller)}.
     */
    public VelocityMotorDevice(HardwareMap hardwareMap, String name) {
        super(hardwareMap, name);
        this.controller = new NullController(0);
        velocity = 0;
    }

    /** Swaps the control law; the target velocity is re-read from the new controller. */
    public void setController(Controller controller) {
        this.controller = controller;
        velocity = this.controller.getTarget();
    }

    /**
     * Sets the target velocity, in ticks per second. Applied via the controller on the next
     * {@link #update()}.
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
        controller.setTarget(velocity);
    }

    @Override
    public void update() {

        double currentTime = System.nanoTime() / 1000000000.0;
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        this.setPower(controller.update(this.getCurrentVelocity(), dt));

        super.update();
    }
}
