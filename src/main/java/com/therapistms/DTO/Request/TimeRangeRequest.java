package com.therapistms.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
class TimeRangeRequest {
    private String startTime; // "09:00"
    private String endTime;   // "10:00"
}
