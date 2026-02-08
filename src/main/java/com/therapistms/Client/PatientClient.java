package com.therapistms.Client;


import com.therapistms.DTO.Request.MedicalRecordUpdateRequest;
import com.therapistms.DTO.Response.MedicalRecordResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
		name ="patient-service",
		url = "${services.patient.url}"
)
public interface PatientClient {
	@GetMapping("/api/patients/check/{userId}")
	boolean checkPatientByUserId(@PathVariable String userId);

	@GetMapping("/api/patients/medical-records/batch")
	List<MedicalRecordResponseDTO> getMedicalRecordsByIds(
			@RequestParam List<String> ids
	);

	@PutMapping("/api/patients/medical-records/{recordId}/therapist")
	Boolean updateMedicalRecord (@PathVariable String recordId,@RequestBody MedicalRecordUpdateRequest medicalRecordResponseDTO);
}
