package com.therapistms.Controller;

import com.therapistms.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("THERAPIST SERVICE UP");
    }

}

