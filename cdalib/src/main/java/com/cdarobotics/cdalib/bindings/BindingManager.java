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
     * Function that adds a binding to the BindingManager class. It will be registered in the map with the id and the Binding. If it already is in the class, then this function will not do anything and return false.
     *
     * @param id The id of the Binding to be added
     * @param condition The supplier for the Binding
     * @return Whether the binding was added
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
     * Function that adds a binding to the BindingManager class. It will be registered in the map with the id and the Binding. If it already is in the class, then this function will not do anything and return false.
     *
     * @param binding The binding to be added
     * @return Whether the binding was added
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
     * Function that adds or replaces an existing Binding in the BindingManager class. It will be registered in the map with the id and the Binding.
     *
     * @param id The id of the Binding to be added
     * @param condition The supplier for the Binding
     */
    public void replaceBinding(String id, BooleanSupplier condition) {
        bindings.put(id, new Binding(condition, id));
    }

    /**
     * Function that adds or replaces an existing Binding in the BindingManager class. It will be registered in the map with the id and the Binding.
     *
     * @param binding The id of the Binding to be added
     */
    public void replaceBinding(Binding binding) {
        bindings.put(binding.getId(), binding);
    }

    /**
     * Function that adds an analog binding to the BindingManager class. It will be registered in the map with the id and the AnalogBinding. If it already is in the class, then this function will not do anything and return false.
     *
     * @param id The id of the Binding to be added
     * @param provider The supplier for the Binding
     * @return Whether the analog binding was added
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
     * Function that adds or replaces an existing AnalogBinding in the BindingManager class. It will be registered in the map with the id and the AnalogBinding.
     *
     * @param id The id of the AnalogBinding to be added
     * @param provider The supplier for the AnalogBinding
     */
    public void replaceAnalog(String id, DoubleSupplier provider) {
        analogs.put(id, new AnalogBinding(provider, id));
    }

    /**
     * Function that adds or replaces an existing AnalogBinding in the BindingManager class. It will be registered in the map with the id and the AnalogBinding.
     *
     * @param analog The binding to be added
     */
    public void replaceAnalog(AnalogBinding analog) {
        analogs.put(analog.getId(), analog);
    }

    /**
     * Function that retrieves the state of a binding using an id. If the binding with that id is not registered inside of this class or the registered binding is null, then this function will always return false.
     * @param id The id of the binding
     * @return The status of the binding, false if it is null or not registered
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
     * Function that retrieves the value of an analog binding using an id. If the binding with that id is not registered inside of this class or the registered binding is null, then this function will always return false.
     * @param id The id of the binding
     * @return The value of the binding, 0 if it is null or not registered
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
