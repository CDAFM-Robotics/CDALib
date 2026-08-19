package com.cdarobotics.cdalib.opmodes.modules;

import com.cdarobotics.cdalib.bindings.Binding;
import com.cdarobotics.cdalib.bindings.BindingManager;

/**
 * A {@link Module} that runs an action whenever its binding fires. The binding is registered with the
 * given {@link BindingManager}, and the action is triggered edge-wise — once each time the binding
 * transitions from inactive to active.
 */
public class BindingModule extends Module {

    private final BindingManager bindingManager;
    private final String id;
    private final Runnable action;

    private boolean lastActive = false;

    /**
     * @param bindingManager the manager the binding is registered with
     * @param binding        the trigger condition
     * @param action         run once on each rising edge of the binding
     */
    public BindingModule(BindingManager bindingManager, Binding binding, Runnable action) {
        this.bindingManager = bindingManager;
        this.bindingManager.replaceBinding(binding);

        this.id = binding.getId();

        this.action = action;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        // Edge-triggered: run the action once each time the binding goes from inactive to active.
        boolean active = bindingManager.checkBinding(id);
        if (active && !lastActive) {
            action.run();
        }
        lastActive = active;
    }

    @Override
    public void stop() {

    }
}
