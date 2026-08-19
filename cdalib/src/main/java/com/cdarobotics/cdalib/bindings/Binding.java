package com.cdarobotics.cdalib.bindings;

import java.util.function.BooleanSupplier;

/**
 * A named boolean condition, typically wired to a gamepad button or a robot state. The condition
 * is evaluated on demand via {@link #getStatus()}.
 */
public class Binding {
    private final BooleanSupplier condition;
    private final String id;

    /**
     * @param condition the boolean condition this binding evaluates
     * @param id        the unique name used to register and look up this binding
     */
    public Binding(BooleanSupplier condition, String id) {
        this.condition = condition;
        this.id = id;
    }

    /** @return the unique id of this binding. */
    public String getId() {
        return id;
    }

    /** @return the underlying condition supplier. */
    public BooleanSupplier getCondition() {
        return condition;
    }

    /** @return the current value of this binding's condition. */
    public boolean getStatus() {
        return condition.getAsBoolean();
    }
}
