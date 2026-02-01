package com.therapistms.DTO.Response;

import com.therapistms.ENUM.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate createdDate;

    private String symptoms;
    private String prescribedTreatment;

    private String medications;
    private String followUpRequired;
    private boolean needTherapy;
    private TherapyPlanDTO therapistPlans;

    private Status sessionStatus;
    private boolean approvedByTherapist;
}
