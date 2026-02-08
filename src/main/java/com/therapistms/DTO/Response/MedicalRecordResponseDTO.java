package com.therapistms.DTO.Response;

import com.therapistms.ENUM.MedicalRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {
    private String medicalRecordId;

    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private LocalDate visitDate;
    private LocalTime appointmentTime;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String symptoms;
    private String prescribedTreatment;

    private String medications;
    private String followUpRequired;
    private boolean needTherapy;
    private TherapyPlanDTO therapistPlans;
    private MedicalRecordStatus sessionMedicalRecordStatus;
    private boolean approvedByTherapist;
}
