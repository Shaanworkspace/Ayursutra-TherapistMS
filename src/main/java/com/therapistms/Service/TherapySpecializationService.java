package com.therapistms.Service;

import com.therapistms.DTO.Response.TherapistResponseDTO;
import com.therapistms.DTO.Response.TherapySpecializationDTO;
import com.therapistms.Entity.Therapist;
import com.therapistms.Entity.TherapySpecialization;
import com.therapistms.Repository.TherapySpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapySpecializationService {

    private final TherapySpecializationRepository specializationRepository;
    private final TherapistService therapistService;

    public List<TherapySpecializationDTO> getAllSpecializations() {
        return specializationRepository.findAll()
                .stream()
                .map(this::TherapistSpecializationToDto)
                .collect(Collectors.toList());
    }

    public TherapySpecializationDTO getSpecializationById(Long id) {
        TherapySpecialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found with id: " + id));
        return TherapistSpecializationToDto(specialization);
    }

    // 🔹 conversion method
    public TherapySpecializationDTO TherapistSpecializationToDto(TherapySpecialization entity) {
        List<TherapistResponseDTO> therapistDTOs = entity.getTherapists()
                .stream()
                .map(this::convertTherapistToDto)
                .collect(Collectors.toList());

        return new TherapySpecializationDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getBenefits(),
                entity.getContraindications(),
                entity.getDuration(),
                entity.getCategory(),
                entity.getTraditionalReference(),
                entity.getCostEstimate(),
                entity.isActive(),
                therapistDTOs
        );
    }

    private TherapistResponseDTO convertTherapistToDto(Therapist entity) {
        return therapistService.therapistToDTO(entity);
    }
}