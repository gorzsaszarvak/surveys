package com.dynata.surveys.domain;

public enum Status {
    NOT_ASKED(1),
    REJECTED(2),
    FILTERED(3),
    COMPLETED(4);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public static Status fromInt(int value) {
        for (Status status : Status.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
