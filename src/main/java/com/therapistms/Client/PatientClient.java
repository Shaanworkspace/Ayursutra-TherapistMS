package com.therapistms.Client;


import com.therapistms.DTO.Response.MedicalRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
		name ="patient-service",
		url = "${services.patient.url}"
)
public interface PatientClient {
	@GetMapping("/api/patients/check/{userId}")
	boolean checkPatientByUserId(@PathVariable String userId);

	@GetMapping("/api/patients/medical-records/therapist/{therapistId}")
	List<MedicalRecord> medicalRecordsByTherapistId(@PathVariable String therapistId);
}
