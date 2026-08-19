package com.cdarobotics.cdalib.devices.sensors;

import android.graphics.Color;

import com.cdarobotics.cdalib.devices.Device;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.reflect.Field;

/**
 * A fast {@link Device} for the REV Color Sensor V3 (Broadcom APDS-9151).
 *
 * <p>The stock SDK path reads each channel with a separate I2C transaction, and I2C devices are
 * <em>not</em> covered by Lynx bulk caching — so polling colour, proximity and distance the normal
 * way costs several blocking round-trips per loop. This device instead pulls proximity plus all
 * three colour channels off the sensor in a <strong>single 14-byte I2C read</strong> of the raw
 * register window (0x08–0x15), then decodes red/green/blue, distance and HSV in software. One
 * hardware transaction per {@link #update()} instead of many is the loop-time win.
 *
 * <p>To reach the raw I2C client the SDK hides behind {@link RevColorSensorV3}, the {@code
 * deviceClient} field is read reflectively once at construction. All getters return the values
 * cached by the most recent {@link #update()}.
 *
 * <p>The channel scaling and the optical-to-distance curve are reproduced from the SDK's own
 * {@code RevColorSensorV3} implementation, so normalized colours and distance match what the stock
 * driver would report at the same {@link #setGain(float) gain}.
 */
public class RevColorSensorV3Device extends Device {

    // Register window, matching the APDS-9151 datasheet layout:
    //   0x08-0x09 (2) proximity   0x0A-0x0C (3) IR
    //   0x0D-0x0F (3) green       0x10-0x12 (3) blue    0x13-0x15 (3) red
    private static final int READ_START = 0x08;
    private static final int READ_LENGTH = 14;

    // Highest value of a 20-bit channel (2^20 - 1), used to normalize colours to [0, 1].
    private static final int CHANNEL_LIMIT = 1048575;

    // Per-channel corrections applied by the stock RevColorSensorV3 driver before normalization.
    private static final double BLUE_SCALE = 1.55;
    private static final double RED_SCALE = 1.07;

    // Inverse-power-law fit converting raw proximity counts to inches: dist = ((raw - c)/a)^(1/b).
    // Constants and cap are copied verbatim from RevColorSensorV3.
    private static final double A_PARAM = 325.961;
    private static final double B_INV_PARAM = -0.75934;
    private static final double C_PARAM = 26.980;
    private static final double MAX_DIST_INCHES = 6.0;

    private final RevColorSensorV3 sensor;
    private final I2cDeviceSynchSimple device;

    private float gain = 20.0f;

    private final NormalizedRGBA colors = new NormalizedRGBA();
    private final float[] hsv = new float[]{0f, 0f, 0f};
    private int redRaw;
    private int greenRaw;
    private int blueRaw;
    private double distanceCm;

    /** Wraps an already-resolved REV Color Sensor V3. */
    public RevColorSensorV3Device(RevColorSensorV3 sensor) {
        this.sensor = sensor;
        this.device = extractDeviceClient(sensor);
        sensor.setGain(gain);
    }

    /** Resolves the REV Color Sensor V3 from the hardware map by configured name. */
    public RevColorSensorV3Device(HardwareMap hardwareMap, String name) {
        this(hardwareMap.get(RevColorSensorV3.class, name));
    }

    /**
     * Reflectively fetches the raw I2C client the SDK stores in {@code deviceClient}. The field
     * lives on a base class ({@code I2cDeviceSynchDevice}), so we climb the hierarchy rather than
     * assuming a fixed depth, which keeps this working across SDK versions.
     */
    private static I2cDeviceSynchSimple extractDeviceClient(RevColorSensorV3 sensor) {
        for (Class<?> c = sensor.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField("deviceClient");
                field.setAccessible(true);
                return (I2cDeviceSynchSimple) field.get(sensor);
            } catch (NoSuchFieldException ignored) {
                // keep climbing
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access RevColorSensorV3 deviceClient", e);
            }
        }
        throw new RuntimeException("Could not locate RevColorSensorV3 deviceClient field");
    }

    /**
     * Sets the software gain applied to the normalized colour channels (default {@code 20}). Higher
     * gain brightens dim readings; this is the same multiplier the stock driver uses, applied here
     * in software, so it does not add a hardware transaction.
     */
    public RevColorSensorV3Device setGain(float gain) {
        this.gain = gain;
        sensor.setGain(gain);
        return this;
    }

    /** @return the gain currently applied to normalized colours. */
    public float getGain() {
        return gain;
    }

    /** @return the normalized RGBA (each channel in [0, 1]) as of the last {@link #update()}. */
    public NormalizedRGBA getColors() {
        return colors;
    }

    /** @return the normalized red channel in [0, 1] as of the last {@link #update()}. */
    public float red() {
        return colors.red;
    }

    /** @return the normalized green channel in [0, 1] as of the last {@link #update()}. */
    public float green() {
        return colors.green;
    }

    /** @return the normalized blue channel in [0, 1] as of the last {@link #update()}. */
    public float blue() {
        return colors.blue;
    }

    /** @return the raw (unnormalized) red channel as of the last {@link #update()}. */
    public int redRaw() {
        return redRaw;
    }

    /** @return the raw (unnormalized) green channel as of the last {@link #update()}. */
    public int greenRaw() {
        return greenRaw;
    }

    /** @return the raw (unnormalized) blue channel as of the last {@link #update()}. */
    public int blueRaw() {
        return blueRaw;
    }

    /** @return the hue in degrees [0, 360) as of the last {@link #update()}. */
    public float getHue() {
        return hsv[0];
    }

    /** @return the saturation in [0, 1] as of the last {@link #update()}. */
    public float getSaturation() {
        return hsv[1];
    }

    /** @return the value (brightness) in [0, 1] as of the last {@link #update()}. */
    public float getValue() {
        return hsv[2];
    }

    /** @return the last measured distance, converted into the requested unit. */
    public double getDistance(DistanceUnit unit) {
        return unit.fromUnit(DistanceUnit.CM, distanceCm);
    }

    /** @return the last measured distance in centimetres. */
    public double getDistanceCm() {
        return distanceCm;
    }

    /** @return the wrapped SDK sensor, for operations not exposed here. */
    public RevColorSensorV3 getSensor() {
        return sensor;
    }

    @Override
    public void update() {
        // One blocking I2C transaction pulls proximity + green + blue + red in a single window.
        byte[] bytes = device.read(READ_START, READ_LENGTH);

        // Each colour channel is a 20-bit value packed little-endian across three bytes.
        greenRaw = ((bytes[7] & 0x0F) << 16) | ((bytes[6] & 0xFF) << 8) | (bytes[5] & 0xFF);
        blueRaw = Range.clip(
                (int) ((((bytes[10] & 0x0F) << 16) | ((bytes[9] & 0xFF) << 8) | (bytes[8] & 0xFF)) * BLUE_SCALE),
                0, CHANNEL_LIMIT);
        redRaw = Range.clip(
                (int) ((((bytes[13] & 0x0F) << 16) | ((bytes[12] & 0xFF) << 8) | (bytes[11] & 0xFF)) * RED_SCALE),
                0, CHANNEL_LIMIT);

        colors.red = Range.clip((redRaw * gain) / CHANNEL_LIMIT, 0f, 1f);
        colors.green = Range.clip((greenRaw * gain) / CHANNEL_LIMIT, 0f, 1f);
        colors.blue = Range.clip((blueRaw * gain) / CHANNEL_LIMIT, 0f, 1f);

        // Proximity counts (11-bit) → distance via the SDK's inverse-power-law fit.
        int rawOptical = (((bytes[1] & 0xFF) << 8) | (bytes[0] & 0xFF)) & 0x7FF;
        distanceCm = DistanceUnit.CM.fromUnit(DistanceUnit.INCH, inchesFromOptical(rawOptical));

        Color.RGBToHSV((int) (colors.red * 255), (int) (colors.green * 255), (int) (colors.blue * 255), hsv);
    }

    /** Converts a raw proximity count to inches, capped at {@value #MAX_DIST_INCHES}. */
    private double inchesFromOptical(int rawOptical) {
        // A negative base can't be raised to a fractional power; below the offset, report the cap.
        if (rawOptical <= C_PARAM) {
            return MAX_DIST_INCHES;
        }
        double dist = Math.pow((rawOptical - C_PARAM) / A_PARAM, B_INV_PARAM);
        return Math.min(dist, MAX_DIST_INCHES);
    }
}
