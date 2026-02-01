package com.therapistms.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistResponseDTO {
    private String userId;
    private String therapistName;
    private String email;
    private String phoneNumber;
    private String qualification;
    private Integer yearsOfExperience;
    private String expertise;
    @Builder.Default
    private String role="THERAPIST";

    List<MedicalRecordResponseDTO> medicalRecords;
}