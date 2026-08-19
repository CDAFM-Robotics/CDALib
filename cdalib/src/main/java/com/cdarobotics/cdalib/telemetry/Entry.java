package com.cdarobotics.cdalib.telemetry;

import java.util.function.Supplier;

public class Entry {

    private final String tag;
    private final String label;
    private final Supplier<String> supplier;

    public Entry(String tag, String label, Supplier<String> supplier) {
        this.tag = tag;
        this.label = label;
        this.supplier = supplier;
    }

    public Entry(String label, Supplier<String> supplier) {
        this.tag = "default";
        this.label = label;
        this.supplier = supplier;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return supplier.get();
    }

    public String getTag() {
        return tag;
    }
}
