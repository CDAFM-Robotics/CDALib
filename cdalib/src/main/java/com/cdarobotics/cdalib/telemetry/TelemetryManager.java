package com.cdarobotics.cdalib.telemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Supplier;

public class TelemetryManager {

    private final Telemetry telemetry;

    private final LinkedList<Entry> entries = new LinkedList<>();

    private final LinkedList<String> displayTags = new LinkedList<>();

    public TelemetryManager(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void addEntry(String tag, String label, Supplier<String> supplier) {
        entries.add(new Entry(tag, label, supplier));
    }

    public void addEntry(String label, Supplier<String> supplier) {
        entries.add(new Entry(label, supplier));
    }

    public void setDisplayTags(String... tags) {
        displayTags.addAll(Arrays.asList(tags));
    }

    public void removeDisplayTags(String... tags) {
        displayTags.removeAll(Arrays.asList(tags));
    }

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
