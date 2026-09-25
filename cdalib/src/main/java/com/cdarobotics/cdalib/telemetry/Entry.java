package com.cdarobotics.cdalib.telemetry;

import java.util.function.Supplier;

/**
 * A single telemetry line: a {@code label} paired with a {@link Supplier} whose value is fetched
 * fresh each time the entry is displayed, grouped under a {@code tag} for selective display by
 * {@link TelemetryManager}.
 */
public class Entry {

    private final String tag;
    private final String label;
    private final Supplier<String> supplier;

    /**
     * Creates an entry with an explicit group tag.
     *
     * @param tag      the group this entry belongs to, used to filter what gets displayed.
     * @param label    the label shown for this entry.
     * @param supplier supplies the current value each time the entry is displayed.
     */
    public Entry(String tag, String label, Supplier<String> supplier) {
        this.tag = tag;
        this.label = label;
        this.supplier = supplier;
    }

    /**
     * Creates an entry in the {@code "default"} group.
     *
     * @param label    the label shown for this entry.
     * @param supplier supplies the current value each time the entry is displayed.
     */
    public Entry(String label, Supplier<String> supplier) {
        this.tag = "default";
        this.label = label;
        this.supplier = supplier;
    }

    /** @return the label shown for this entry. */
    public String getLabel() {
        return label;
    }

    /** @return the current value, re-fetched from the supplier on each call. */
    public String getValue() {
        return supplier.get();
    }

    /** @return the group tag this entry belongs to. */
    public String getTag() {
        return tag;
    }
}
