package com.therapistms.DTO.Request;

import com.therapistms.ENUM.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordUpdateRequest {
	private Boolean needTherapy;
	private Status sessionStatus;
}
