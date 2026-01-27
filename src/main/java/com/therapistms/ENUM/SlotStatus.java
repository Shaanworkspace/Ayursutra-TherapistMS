package com.therapistms.ENUM;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SlotStatus {
    AVAILABLE,
    BOOKED,
    CANCELLED,
    LOCKED
}
