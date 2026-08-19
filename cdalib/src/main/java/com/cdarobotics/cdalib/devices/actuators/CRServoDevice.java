package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link Device} wrapping a {@link CRServo} (continuous-rotation servo). Power commands are cached
 * and only written to hardware in {@link #update()} when the target has changed.
 */
public class CRServoDevice extends Device {
    private final CRServo servo;

    private double targetPower = 0.0;
    private boolean dirty = true;

    /** Wraps an already-resolved CR servo. */
    public CRServoDevice(CRServo servo) {
        this.servo = servo;
    }

    /** Resolves the CR servo from the hardware map by configured name. */
    public CRServoDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(CRServo.class, name));
    }

    /** Sets the power to command on the next {@link #update()}. Range [-1.0, 1.0]. */
    public CRServoDevice setPower(double power) {
        if (power != targetPower) {
            targetPower = power;
            dirty = true;
        }
        return this;
    }

    /** @return the last commanded power (not necessarily yet flushed to hardware). */
    public double getPower() {
        return targetPower;
    }

    /** Sets the CR servo's direction. */
    public CRServoDevice setDirection(DcMotorSimple.Direction direction) {
        servo.setDirection(direction);
        return this;
    }

    /** @return the wrapped SDK CR servo, for operations not exposed here. */
    public CRServo getServo() {
        return servo;
    }

    @Override
    public void update() {
        if (dirty) {
            servo.setPower(targetPower);
            dirty = false;
        }
    }
}
