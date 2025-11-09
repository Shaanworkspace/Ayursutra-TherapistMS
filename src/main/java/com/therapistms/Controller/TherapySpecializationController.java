package com.therapistms.Controller;

import com.therapistms.DTO.Response.TherapySpecializationDTO;
import com.therapistms.Entity.TherapySpecialization;
import com.therapistms.Repository.TherapySpecializationRepository;
import com.therapistms.Service.TherapySpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
public class TherapySpecializationController {

    private final TherapySpecializationService specializationService;
    private final TherapySpecializationRepository specializationRepository;

    // ========================================
    //  1. Fetch all specializations (DTO list)
    // ========================================
    @GetMapping
    public ResponseEntity<List<TherapySpecializationDTO>> getAllSpecializations() {
        List<TherapySpecializationDTO> dtos = specializationService.getAllSpecializations();
        return ResponseEntity.ok(dtos);
    }

    // ========================================
    //  2. Fetch specialization by ID (DTO)
    // ========================================
    @GetMapping("/{id}")
    public ResponseEntity<TherapySpecializationDTO> getSpecializationById(@PathVariable Long id) {
        TherapySpecializationDTO dto = specializationService.getSpecializationById(id);
        return ResponseEntity.ok(dto);
    }

    // ========================================
    //  3. Create a new specialization (Admin use)
    // ========================================
    @PostMapping
    public ResponseEntity<TherapySpecialization> createSpecialization(@RequestBody TherapySpecialization specialization) {
        TherapySpecialization saved = specializationRepository.save(specialization);
        return ResponseEntity
                .created(URI.create("/api/specializations/" + saved.getId()))
                .body(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TherapySpecialization>> createSpecializations(@RequestBody List<TherapySpecialization> specializations) {
        if (specializations == null || specializations.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<TherapySpecialization> saved = specializationRepository.saveAll(specializations);
        return ResponseEntity.ok(saved);
    }

    // ========================================
    //  4. Update existing specialization
    // ========================================
    @PutMapping("/{id}")
    public ResponseEntity<TherapySpecialization> updateSpecialization(
            @PathVariable Long id,
            @RequestBody TherapySpecialization updated) {

        TherapySpecialization existing = specializationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found with ID: " + id));

        // update only provided fields
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getBenefits() != null) existing.setBenefits(updated.getBenefits());
        if (updated.getContraindications() != null) existing.setContraindications(updated.getContraindications());
        if (updated.getDuration() != null) existing.setDuration(updated.getDuration());
        if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
        if (updated.getTraditionalReference() != null) existing.setTraditionalReference(updated.getTraditionalReference());
        if (updated.getCostEstimate() != null) existing.setCostEstimate(updated.getCostEstimate());
        existing.setActive(updated.isActive());

        TherapySpecialization saved = specializationRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ========================================
    //  5. Delete specialization (Admin use)
    // ========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        TherapySpecialization existing = specializationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found with ID: " + id));
        specializationRepository.delete(existing);
        return ResponseEntity.noContent().build();
    }
}