package com.therapistms.DTO.Request;

import com.therapistms.ENUM.MedicalRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordUpdateRequest {
	private boolean needTherapy = false;
	private MedicalRecordStatus sessionMedicalRecordStatus;
}
