package com.cdarobotics.cdalib.devices.sensors;

import com.cdarobotics.cdalib.devices.Device;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A {@link Device} wrapping a {@link ColorSensor}. All four channels (plus packed ARGB) are read
 * once per {@link #update()} and cached, so a subsystem can inspect several channels in a loop for
 * the cost of a single hardware read.
 *
 * <p>Getters reflect the values from the most recent {@link #update()}.
 */
public class ColorSensorDevice extends Device {
    private final ColorSensor sensor;

    private int red;
    private int green;
    private int blue;
    private int alpha;
    private int argb;

    /** Wraps an already-resolved color sensor. */
    public ColorSensorDevice(ColorSensor sensor) {
        this.sensor = sensor;
    }

    /** Resolves the color sensor from the hardware map by configured name. */
    public ColorSensorDevice(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(ColorSensor.class, name));
    }

    /** @return the red channel as of the last {@link #update()}. */
    public int red() {
        return red;
    }

    /** @return the green channel as of the last {@link #update()}. */
    public int green() {
        return green;
    }

    /** @return the blue channel as of the last {@link #update()}. */
    public int blue() {
        return blue;
    }

    /** @return the alpha (overall brightness) channel as of the last {@link #update()}. */
    public int alpha() {
        return alpha;
    }

    /** @return the packed ARGB color as of the last {@link #update()}. */
    public int argb() {
        return argb;
    }

    /** Turns the sensor's onboard LED on or off. */
    public ColorSensorDevice enableLed(boolean enable) {
        sensor.enableLed(enable);
        return this;
    }

    /** @return the wrapped SDK sensor, for operations not exposed here. */
    public ColorSensor getSensor() {
        return sensor;
    }

    @Override
    public void update() {
        red = sensor.red();
        green = sensor.green();
        blue = sensor.blue();
        alpha = sensor.alpha();
        argb = sensor.argb();
    }
}
