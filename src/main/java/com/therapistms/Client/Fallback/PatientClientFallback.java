package com.therapistms.Client.Fallback;

import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.MedicalRecordUpdateRequest;
import com.therapistms.DTO.Response.MedicalRecord;
import com.therapistms.DTO.Response.MedicalRecordResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PatientClientFallback implements PatientClient {

	@Override
	public boolean checkPatientByUserId(String userId) {
		log.warn("Falling back from Therapist MS as no patient found by user id : {}",userId);
		return false;
	}

	@Override
	public List<MedicalRecordResponseDTO> getMedicalRecordsByIds(List<String> ids) {
		log.warn("Falling back from Therapist MS as no medical records found by medical record ids : {}",ids);
		return List.of();
	}

	@Override
	public Boolean checkMedicalRecordByMedicalId(String medicalId) {
		log.warn("Falling back from Therapist MS as no medical record found by medical record id : {}",medicalId);
		return false;
	}

	@Override
	public Boolean updateMedicalRecord(String recordId, MedicalRecordUpdateRequest medicalRecordResponseDTO) {
		log.warn("FallBack");
		return null;
	}
}
