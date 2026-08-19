package com.cdarobotics.cdalib.bindings;

import java.util.function.BooleanSupplier;

/**
 * A {@link Binding} that flips its state on each rising edge of the underlying condition.
 * The raw condition is treated as a "button": every time it transitions from false to true,
 * the toggle inverts. {@link #getStatus()} returns the latched toggle state, not the raw button.
 *
 * <p>Because edge detection is stateful, {@link #getStatus()} should be polled exactly once per loop.
 */
public class ToggleBinding extends Binding {
    private boolean toggled = false;
    private boolean lastPressed = false;

    /**
     * @param condition the momentary "button" condition whose rising edges flip the toggle
     * @param id        the unique name used to register and look up this binding
     */
    public ToggleBinding(BooleanSupplier condition, String id) {
        super(condition, id);
    }

    @Override
    public boolean getStatus() {
        boolean pressed = super.getStatus();
        if (pressed && !lastPressed) {
            toggled = !toggled;
        }
        lastPressed = pressed;
        return toggled;
    }
}
