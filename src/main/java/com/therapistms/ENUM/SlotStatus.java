package com.therapistms.ENUM;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SlotStatus {
    AVAILABLE,
    BOOKED,
    CANCELLED;

    @JsonCreator
    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
