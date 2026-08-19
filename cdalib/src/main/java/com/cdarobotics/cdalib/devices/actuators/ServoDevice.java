package com.cdarobotics.cdalib.devices.actuators;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A {@link Device} wrapping a {@link Servo}. Position commands are cached and only written to
 * hardware in {@link #update()} when the target has changed, collapsing redundant bus writes.
 */
public class ServoDevice extends Device {
    private final Servo servo;

    private double targetPosition;
    private boolean dirty = true;

    /** Wraps an already-resolved servo. */
    public ServoDevice(Servo servo) {
        this.servo = servo;
        this.targetPosition = servo.getPosition();
    }

    /** Resolves the servo from the hardware map by configured name. */
    public ServoDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(Servo.class, name));
    }

    /** Sets the position to command on the next {@link #update()}. Range [0.0, 1.0]. */
    public ServoDevice setPosition(double position) {
        if (position != targetPosition) {
            targetPosition = position;
            dirty = true;
        }
        return this;
    }

    /** @return the last commanded position (not necessarily yet flushed to hardware). */
    public double getPosition() {
        return targetPosition;
    }

    /** Sets the servo's direction. */
    public ServoDevice setDirection(Servo.Direction direction) {
        servo.setDirection(direction);
        return this;
    }

    /** Constrains the usable range of the servo. See {@link Servo#scaleRange(double, double)}. */
    public ServoDevice scaleRange(double min, double max) {
        servo.scaleRange(min, max);
        return this;
    }

    /** @return the wrapped SDK servo, for operations not exposed here. */
    public Servo getServo() {
        return servo;
    }

    @Override
    public void update() {
        if (dirty) {
            servo.setPosition(targetPosition);
            dirty = false;
        }
    }
}
