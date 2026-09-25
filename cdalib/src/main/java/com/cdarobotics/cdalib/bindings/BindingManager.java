package com.cdarobotics.cdalib.bindings;

import java.util.TreeMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * A registry of named {@link Binding}s (boolean conditions) and {@link AnalogBinding}s (analog
 * inputs). Controls are registered by id and queried by id, decoupling the wiring of an input from
 * the code that reacts to it.
 */
public class BindingManager {
    TreeMap<String, Binding> bindings = new TreeMap<>();

    TreeMap<String, AnalogBinding> analogs = new TreeMap<>();

    /** Creates an empty binding manager. */
    public BindingManager() {

    }

    /**
     * Registers a new {@link Binding} under the given id. Does nothing if a binding with that id is
     * already registered.
     *
     * @param id        the id to register the binding under
     * @param condition the condition supplier for the binding
     * @return {@code true} if the binding was added, {@code false} if the id was already taken
     */
    public boolean addBinding(String id, BooleanSupplier condition) {
        if (!bindings.containsKey(id)) {
            bindings.put(id, new Binding(condition, id));
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Registers the given {@link Binding} under its own id. Does nothing if a binding with that id
     * is already registered.
     *
     * @param binding the binding to add
     * @return {@code true} if the binding was added, {@code false} if the id was already taken
     */
    public boolean addBinding(Binding binding) {
        if (!bindings.containsKey(binding.getId())) {
            bindings.put(binding.getId(), binding);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Registers a {@link Binding} under the given id, overwriting any existing binding with that id.
     *
     * @param id        the id to register the binding under
     * @param condition the condition supplier for the binding
     */
    public void replaceBinding(String id, BooleanSupplier condition) {
        bindings.put(id, new Binding(condition, id));
    }

    /**
     * Registers the given {@link Binding} under its own id, overwriting any existing binding with
     * that id.
     *
     * @param binding the binding to add
     */
    public void replaceBinding(Binding binding) {
        bindings.put(binding.getId(), binding);
    }

    /**
     * Registers a new {@link AnalogBinding} under the given id. Does nothing if an analog binding
     * with that id is already registered.
     *
     * @param id       the id to register the analog binding under
     * @param provider the value supplier for the analog binding
     * @return {@code true} if the analog binding was added, {@code false} if the id was already taken
     */
    public boolean addAnalog(String id, DoubleSupplier provider) {
        if (!analogs.containsKey(id)) {
            analogs.put(id, new AnalogBinding(provider, id));
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Registers an {@link AnalogBinding} under the given id, overwriting any existing analog binding
     * with that id.
     *
     * @param id       the id to register the analog binding under
     * @param provider the value supplier for the analog binding
     */
    public void replaceAnalog(String id, DoubleSupplier provider) {
        analogs.put(id, new AnalogBinding(provider, id));
    }

    /**
     * Registers the given {@link AnalogBinding} under its own id, overwriting any existing analog
     * binding with that id.
     *
     * @param analog the analog binding to add
     */
    public void replaceAnalog(AnalogBinding analog) {
        analogs.put(analog.getId(), analog);
    }

    /**
     * Evaluates the {@link Binding} registered under the given id.
     *
     * @param id the id of the binding
     * @return the binding's current status, or {@code false} if no non-null binding is registered
     *         under that id
     */
    public boolean checkBinding(String id) {
        if (bindings.containsKey(id) && bindings.get(id) != null) {
            return bindings.get(id).getStatus();
        }
        else {
            return false;
        }
    }

    /**
     * Reads the value of the {@link AnalogBinding} registered under the given id.
     *
     * @param id the id of the analog binding
     * @return the analog binding's current value, or {@code 0} if no non-null analog binding is
     *         registered under that id
     */
    public double checkAnalog(String id) {
        if (analogs.containsKey(id) && analogs.get(id) != null) {
            return analogs.get(id).getValue();
        }
        else {
            return 0;
        }
    }
}
