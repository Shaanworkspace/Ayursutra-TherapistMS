package com.therapistms.Controller;


import com.therapistms.DTO.Response.ScheduleSlotDTO;
import com.therapistms.DTO.Response.TherapistResponseDTO;
import com.therapistms.DTO.Response.WeeklyScheduleDTO;
import com.therapistms.Entity.ScheduleSlot;
import com.therapistms.Entity.Therapist;
import com.therapistms.Service.ScheduleService;
import com.therapistms.Service.TherapistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@RequiredArgsConstructor
public class TherapistController {

    private final TherapistService therapistService;
    private final ScheduleService scheduleService;
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("THERAPIST SERVICE UP");
    }

    @GetMapping
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> getTherapistById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(therapistService.getTherapistById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/expertise/{exp}")
    public List<TherapistResponseDTO> getTherapistsByExpertise(@PathVariable String exp) {
        return therapistService.getTherapistsByExpertise(exp);
    }


    @PostMapping
    public ResponseEntity<TherapistResponseDTO> createTherapist(@RequestBody Therapist therapist) {
        Therapist saved = therapistService.addTherapist(therapist);
        return ResponseEntity.ok(therapistService.therapistToDTO(saved));
    }

    @PostMapping("/bulk")
    public List<Therapist> createTherapists(@RequestBody List<Therapist> therapists) {
        return therapistService.createTherapists(therapists);
    }
    @PutMapping("/{therapistId}/book-slot/{slotId}")
    public ResponseEntity<?> bookSlot(
            @PathVariable Long therapistId,
            @PathVariable Long slotId,
            @RequestParam Long patientId
    ) {
        try {
            ScheduleSlot booked = scheduleService.bookSlot(therapistId, slotId, patientId);

            ScheduleSlotDTO dto = new ScheduleSlotDTO(
                    booked.getId(),
                    booked.getDate(),
                    booked.getStartTime(),
                    booked.getEndTime(),
                    booked.getStatus(),
                    booked.getBookedByPatientId() != null ? booked.getBookedByPatientId() : null
            );

            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTherapist(@PathVariable Long id) {
        try {
            therapistService.deleteTherapist(id);
            return ResponseEntity.ok("✅ Therapist deleted with id"+id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}