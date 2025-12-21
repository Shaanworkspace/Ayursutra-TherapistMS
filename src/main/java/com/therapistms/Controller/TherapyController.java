package com.therapistms.Controller;


import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Service.TherapyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapies")
public class TherapyController {

    @Autowired
    private TherapyPlanService therapyService;
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("THERAPIST SERVICE UP");
    }

    @PostMapping
    public TherapyPlan createTherapy(@RequestBody TherapyPlan therapy) {
        return therapyService.assignTherapy(therapy);
    }

    @GetMapping
    public List<TherapyPlan> getAllTherapies() {
        return therapyService.getAllTherapies();
    }


}
