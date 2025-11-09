package com.therapistms.Service;


import com.therapistms.DTO.Response.ScheduleSlotDTO;
import com.therapistms.DTO.Response.TherapistResponseDTO;
import com.therapistms.DTO.Response.TherapyPlanDTO;
import com.therapistms.DTO.Response.TherapySpecializationDTO;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapistService {

    private final TherapistRepository therapistRepository;
    private final ScheduleService scheduleService;

    public Therapist addTherapist(Therapist therapist) {
        Therapist saved = therapistRepository.save(therapist);

        // Auto create availability slots (8-16, 1 hour) for a week starting from registration date
        LocalDate startDate = saved.getRegistrationDate();

        List<LocalTime[]> defaultRanges = List.of(
                new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(9, 0)},
                new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(10, 0)},
                new LocalTime[]{LocalTime.of(10, 0), LocalTime.of(11, 0)},
                new LocalTime[]{LocalTime.of(11, 0), LocalTime.of(12, 0)},
                new LocalTime[]{LocalTime.of(12, 0), LocalTime.of(13, 0)},
                new LocalTime[]{LocalTime.of(13, 0), LocalTime.of(14, 0)},
                new LocalTime[]{LocalTime.of(14, 0), LocalTime.of(15, 0)},
                new LocalTime[]{LocalTime.of(15, 0), LocalTime.of(16, 0)}
        );

        scheduleService.generateWeeklySchedule(saved.getId(), startDate, defaultRanges);

        return saved;
    }

    //  Bulk add therapists
    public List<Therapist> createTherapists(List<Therapist> therapists) {
        return therapistRepository.saveAll(therapists);
    }

    //  Get all therapists
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistRepository.findAll().stream()
                .map(this::therapistToDTO)
                .collect(Collectors.toList());
    }

    //  Get therapist by ID
    public TherapistResponseDTO getTherapistById(Long id) {
        Therapist therapist = therapistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Therapist not found with id " + id));
        return therapistToDTO(therapist);
    }

    //  Get therapists by expertise
    public List<TherapistResponseDTO> getTherapistsByExpertise(String expertise) {
        return therapistRepository.findByExpertise(expertise).stream()
                .map(this::therapistToDTO)
                .collect(Collectors.toList());
    }

    //  Login (basic example, don’t use plain-text passwords in real systems!)
    public Therapist login(String email, String password) {
        Therapist therapist = therapistRepository.findByEmail(email);
        if (therapist == null) {
            throw new IllegalArgumentException("❌ No Therapist FOUND with email : " + email);
        }
        if (!therapist.getPassword().equals(password)) {
            throw new IllegalArgumentException("❌ Invalid password");
        }
        return therapist;
    }

    //  Delete therapist
    public void deleteTherapist(Long id) {
        if (!therapistRepository.existsById(id)) {
            throw new IllegalArgumentException("❌ Therapist not found with id : " + id);
        }
        therapistRepository.deleteById(id);
    }

    //  Entity-to-DTO mapper (no recursion!)
    public TherapistResponseDTO therapistToDTO(Therapist therapist) {

        // TherapyPlans → DTO
        List<TherapyPlanDTO> planDTOs = therapist.getTherapyPlans().stream()
                .map(plan -> new TherapyPlanDTO(
                        plan.getId(),
                        plan.getTherapyType(),
                        plan.getDurationDays(),
                        plan.getFrequencyPerDay(),
                        plan.getPrecautions(),
                        plan.isApproved(),
                        plan.getPatientId() != null ? plan.getPatientId() : null,
                        plan.getDoctorId() != null ? plan.getDoctorId(): null
                )).collect(Collectors.toList());

        // ScheduleSlots → DTO
        List<ScheduleSlotDTO> slotDTOs = therapist.getScheduleSlots().stream()
                .map(slot -> new ScheduleSlotDTO(
                        slot.getId(),
                        slot.getDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getStatus(),
                        slot.getBookedByPatientId() != null ? slot.getBookedByPatientId(): null
                )).collect(Collectors.toList());

        // Specializations → DTO (⚡ keep lightweight, don’t include therapists)
        List<TherapySpecializationDTO> specDTOs = therapist.getSpecializations().stream()
                .map(spec -> new TherapySpecializationDTO(
                        spec.getId(),
                        spec.getName(),
                        spec.getDescription(),
                        spec.getBenefits(),
                        spec.getContraindications(),
                        spec.getDuration(),
                        spec.getCategory(),
                        spec.getTraditionalReference(),
                        spec.getCostEstimate(),
                        spec.isActive(),
                        null // don’t pass therapist list here to avoid recursion
                )).collect(Collectors.toList());

        // Build full detailed TherapistResponseDTO
        return new TherapistResponseDTO(
                therapist.getId(),
                therapist.getFirstName(),
                therapist.getLastName(),
                therapist.getEmail(),
                therapist.getPhoneNumber(),
                therapist.getQualification(),
                therapist.getYearsOfExperience(),
                therapist.getExpertise(),
                therapist.getLanguagesSpoken(),
                therapist.getClinicLocation(),
                therapist.getBio(),
                planDTOs,
                slotDTOs,
                specDTOs
        );
    }
}