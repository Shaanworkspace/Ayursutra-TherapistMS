package com.therapistms.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TimeRangeRequest {
    private String startTime; // "09:00"
    private String endTime;   // "10:00"
}
