package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * A {@link Device} wrapping a {@link TouchSensor}. The sensor is read once per {@link #update()} and
 * the result is cached, so any number of {@link #isPressed()} calls in a loop cost one hardware read.
 *
 * <p>Getters reflect the value from the most recent {@link #update()}.
 */
public class TouchSensorDevice extends Device {
    private final TouchSensor sensor;

    private boolean pressed;
    private double value;

    /** Wraps an already-resolved touch sensor. */
    public TouchSensorDevice(TouchSensor sensor) {
        this.sensor = sensor;
    }

    /** Resolves the touch sensor from the hardware map by configured name. */
    public TouchSensorDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(TouchSensor.class, name));
    }

    /** @return whether the sensor was pressed as of the last {@link #update()}. */
    public boolean isPressed() {
        return pressed;
    }

    /** @return the raw sensor value (0.0–1.0 for analog touch sensors) as of the last {@link #update()}. */
    public double getValue() {
        return value;
    }

    /** @return the wrapped SDK sensor, for operations not exposed here. */
    public TouchSensor getSensor() {
        return sensor;
    }

    @Override
    public void update() {
        pressed = sensor.isPressed();
        value = sensor.getValue();
    }
}
