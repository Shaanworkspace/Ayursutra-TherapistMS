package com.therapistms.Controller;


import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.RegisterRequestDTO;
import com.therapistms.DTO.Response.MedicalRecordResponseDTO;
import com.therapistms.DTO.Response.TherapistProfileDTO;
import com.therapistms.DTO.Response.TherapistResponseDTO;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Service.TherapistService;
import com.therapistms.Service.TherapyPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@RequiredArgsConstructor
@Slf4j
public class TherapistController {

    private final TherapistService therapistService;
    private final TherapyPlanService therapyPlanService;
    private final TherapistRepository therapistRepository;
    private final PatientClient patientClient;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("THERAPIST SERVICE UP");
    }

    @GetMapping
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }


    @GetMapping("/medical-records/{therapistId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> medicalRecordsByTherapistId(@PathVariable String therapistId) {
        try {
            List<String> medicalIds = therapyPlanService.getMedicalIdsByTherapistId(therapistId);

            if(medicalIds.isEmpty()){
                log.warn("No medical records");
                return ResponseEntity.ok(List.of());
            }
            List<MedicalRecordResponseDTO> record = patientClient.getMedicalRecordsByIds(medicalIds);
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/profile/me")
    public ResponseEntity<TherapistProfileDTO> getMyProfile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        boolean exist = therapistRepository.existsByEmail(email);
        if (!exist){
            throw new RuntimeException(
                    "Therapist not Exist for email: " + email
            );
        }
        log.info("Therapist controller METHOD : GET , REQUEST : profile/me , principle of authentication with Email :{} ",email);
        Therapist therapist = therapistRepository.findByEmail(email);

        if (therapist == null) {
            log.warn("Therapist not found for email: {}" , email);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(therapistService.therapistToProfileDTO(therapist));
    }

    @GetMapping("/exist/{email}")
    public boolean checkTherapistByEmailId(@PathVariable String email) {
        try {
	        return therapistRepository.existsByEmail(email);
        } catch (RuntimeException e) {
            log.info("No Therapist");
            return false;
        }
    }



    @PostMapping
    public ResponseEntity<TherapistResponseDTO> createTherapist(@RequestBody RegisterRequestDTO therapist) {
        Therapist saved = therapistService.addTherapist(therapist);
        return ResponseEntity.ok(therapistService.therapistToDTO(saved));
    }

    @PostMapping("/bulk")
    public List<Therapist> createTherapists(@RequestBody List<Therapist> therapists) {
        return therapistService.createTherapists(therapists);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTherapist(@PathVariable Long id) {
        try {
            therapistService.deleteTherapist(id);
            return ResponseEntity.ok("Therapist deleted with id"+id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}