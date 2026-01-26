package com.therapistms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    private String medicalRecordId;
    private String doctorId;
    private String patientId;
    private String therapistId;
    private String patientName;
    private LocalDate visitDate;
    private LocalDate createdDate;
}
