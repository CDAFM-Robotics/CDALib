package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

/**
 * A {@link Device} wrapping a {@link DcMotorEx}. Power commands are cached and only written to
 * hardware in {@link #update()}, and only when the value has changed — this collapses redundant
 * bus writes so setting the same power every loop costs nothing.
 *
 * <p>Configuration and read setters return {@code this} for fluent chaining.
 */
public class MotorDevice extends Device {
    private final DcMotorEx motor;

    /** Power changes smaller than this (vs. the value last written to hardware) skip the write. */
    private static final double DEFAULT_WRITE_THRESHOLD = 0.005;

    private double targetPower = 0.0;
    private double lastWritten = Double.NaN;   // NaN => nothing written to hardware yet
    private double writeThreshold = DEFAULT_WRITE_THRESHOLD;

    /** Wraps an already-resolved motor. */
    public MotorDevice(DcMotorEx motor) {
        this.motor = motor;
    }

    /** Resolves the motor from the hardware map by configured name. */
    public MotorDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(DcMotorEx.class, name));
    }

    /** Sets the power to command on the next {@link #update()}. Range [-1.0, 1.0]. */
    public MotorDevice setPower(double power) {
        targetPower = power;
        return this;
    }

    /**
     * Sets the minimum power change (relative to the value last written to hardware) that triggers
     * an actual write in {@link #update()}. Larger values collapse more redundant Lynx writes —
     * cutting loop time when the command is steady — at the cost of coarser resolution. Set to
     * {@code 0} for exact writes (every change flushed). Defaults to {@value #DEFAULT_WRITE_THRESHOLD}.
     */
    public MotorDevice setWriteThreshold(double threshold) {
        writeThreshold = threshold;
        return this;
    }

    /** @return the last commanded power (not necessarily yet flushed to hardware). */
    public double getPower() {
        return targetPower;
    }

    /** @return the encoder position, in ticks. */
    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    /** @return the current velocity, in ticks per second. */
    public double getVelocity() {
        return motor.getVelocity();
    }

    /** @return the motor current draw in the given unit. */
    public double getCurrent(CurrentUnit unit) {
        return motor.getCurrent(unit);
    }

    /** Sets the motor's rotation direction. */
    public MotorDevice setDirection(DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
        return this;
    }

    /** Sets what the motor does at zero power — {@code BRAKE} or {@code FLOAT}. */
    public MotorDevice setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        motor.setZeroPowerBehavior(behavior);
        return this;
    }

    /** Sets the run mode (e.g. {@code RUN_USING_ENCODER}, {@code RUN_TO_POSITION}). */
    public MotorDevice setMode(DcMotor.RunMode mode) {
        motor.setMode(mode);
        return this;
    }

    /** @return the wrapped SDK motor, for operations not exposed here. */
    public DcMotorEx getMotor() {
        return motor;
    }

    @Override
    public void update() {
        // Skip the (blocking) Lynx write when the command hasn't moved meaningfully since the last
        // one — joystick jitter and steady holds then cost no bus transaction. Always flush the
        // first command and an exact stop (0.0), so the motor never creeps or lags on those.
        boolean firstWrite = Double.isNaN(lastWritten);
        boolean stopping = targetPower == 0.0 && lastWritten != 0.0;
        if (firstWrite || stopping || Math.abs(targetPower - lastWritten) >= writeThreshold) {
            motor.setPower(targetPower);
            lastWritten = targetPower;
        }
    }
}
