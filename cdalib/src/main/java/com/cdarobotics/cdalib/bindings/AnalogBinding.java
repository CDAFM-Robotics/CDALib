package com.cdarobotics.cdalib.bindings;

import java.util.function.DoubleSupplier;

/**
 * A named analog input (e.g. a trigger pull or joystick axis), evaluated on demand via
 * {@link #getValue()}.
 */
public class AnalogBinding {
    private final DoubleSupplier provider;
    private final String id;

    /**
     * @param provider the analog value source this binding reads
     * @param id       the unique name used to register and look up this binding
     */
    public AnalogBinding(DoubleSupplier provider, String id) {
        this.provider = provider;
        this.id = id;
    }

    /** @return the unique id of this analog binding. */
    public String getId() {
        return id;
    }

    /** @return the underlying value supplier. */
    public DoubleSupplier getProvider() {
        return provider;
    }

    /** @return the current value of this analog input. */
    public double getValue() {
        return provider.getAsDouble();
    }
}
