package com.therapistms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyScheduleDTO {
    private String dayOfWeek; // e.g. MONDAY
    private List<ScheduleSlotDTO> slots;
}