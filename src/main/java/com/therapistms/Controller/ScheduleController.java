package com.therapistms.Controller;


import com.therapistms.DTO.Response.WeeklyScheduleDTO;
import com.therapistms.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/generate-week/{therapistId}")
    public ResponseEntity<List<WeeklyScheduleDTO>> generateWeeklySlots(
            @PathVariable Long therapistId,
            @RequestParam String startDate, // YYYY-MM-DD (monday for example)
            @RequestBody List<TimeRangeRequest> slotTimes) {

        LocalDate start = LocalDate.parse(startDate);

        // convert request into LocalTime[]
        List<LocalTime[]> ranges = slotTimes.stream()
                .map(t -> new LocalTime[]{LocalTime.parse(t.getStartTime()), LocalTime.parse(t.getEndTime())})
                .toList();

        return ResponseEntity.ok(scheduleService.generateWeeklySchedule(therapistId, start, ranges));
    }
}

