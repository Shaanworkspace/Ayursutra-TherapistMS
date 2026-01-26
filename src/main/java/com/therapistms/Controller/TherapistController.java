package com.therapistms.Controller;


import com.therapistms.DTO.Request.RegisterRequestDTO;
import com.therapistms.DTO.Response.TherapistResponseDTO;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Service.TherapistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TherapistRepository therapistRepository;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("THERAPIST SERVICE UP");
    }

    @GetMapping
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }


    @GetMapping("/profile/me")
    public TherapistResponseDTO getMyProfile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        boolean exist = therapistRepository.existsByEmail(email);
        if (!exist){
            throw new RuntimeException(
                    "Therapist not Exist for email: " + email
            );
        }
        log.info("Therapist controller METHOD : GET , REQUEST : profile/me , principle of authentication with Email :{} ",email);
        Therapist therapist = therapistRepository.findByEmail(email);

        if (therapist == null) throw new RuntimeException("Therapist not found for email: " + email);
        return therapistService.therapistToDTO(therapist);
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