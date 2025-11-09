package com.therapistms.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapistResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String qualification;
    private Integer yearsOfExperience;
    private String expertise;
    private String languagesSpoken;
    private String clinicLocation;
    private String bio;


    private List<TherapyPlanDTO> therapyPlanIds;
    private List<ScheduleSlotDTO> scheduleSlots;
    private List<TherapySpecializationDTO> specializations;
}