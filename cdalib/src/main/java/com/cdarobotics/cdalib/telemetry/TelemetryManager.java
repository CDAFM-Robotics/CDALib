package com.cdarobotics.cdalib.telemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Manages a set of {@link Entry telemetry entries} and controls which are pushed to the driver
 * station. Entries are grouped by tag; only entries whose tag is in the active display set (see
 * {@link #setDisplayTags(String...)}) are shown when {@link #displayTelemetry()} is called.
 */
public class TelemetryManager {

    private final Telemetry telemetry;

    private final LinkedList<Entry> entries = new LinkedList<>();

    private final LinkedList<String> displayTags = new LinkedList<>();

    /**
     * Creates a manager that writes to the given telemetry backend.
     *
     * @param telemetry the OpMode telemetry to render entries to.
     */
    public TelemetryManager(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    /** Registers an existing entry. */
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    /**
     * Builds and registers an entry with an explicit group tag.
     *
     * @param tag      the group tag for the entry.
     * @param label    the label shown for the entry.
     * @param supplier supplies the entry's current value on each display.
     */
    public void addEntry(String tag, String label, Supplier<String> supplier) {
        entries.add(new Entry(tag, label, supplier));
    }

    /**
     * Builds and registers an entry in the {@code "default"} group.
     *
     * @param label    the label shown for the entry.
     * @param supplier supplies the entry's current value on each display.
     */
    public void addEntry(String label, Supplier<String> supplier) {
        entries.add(new Entry(label, supplier));
    }

    /** Adds the given tags to the set of groups shown by {@link #displayTelemetry()}. */
    public void setDisplayTags(String... tags) {
        displayTags.addAll(Arrays.asList(tags));
    }

    /** Removes the given tags from the set of groups shown by {@link #displayTelemetry()}. */
    public void removeDisplayTags(String... tags) {
        displayTags.removeAll(Arrays.asList(tags));
    }

    /**
     * Renders every registered entry whose tag is in the active display set, then pushes the batch
     * to the driver station. Values are re-fetched from each entry's supplier as they are rendered.
     */
    public void displayTelemetry() {
        for (String tag : displayTags) {
            for (Entry entry : entries) {
                if (entry.getTag().equals(tag)) {
                    displayEntry(entry);
                }
            }
        }

        telemetry.update();
    }

    private void displayEntry(Entry entry) {
        telemetry.addData("[" + entry.getTag() + "] " + entry.getLabel(), entry.getValue());
    }
}
