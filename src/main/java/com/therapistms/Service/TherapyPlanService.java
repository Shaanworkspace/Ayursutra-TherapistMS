package com.therapistms.Service;

import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.MedicalRecordUpdateRequest;
import com.therapistms.DTO.Request.TherapyPlanRequest;
import com.therapistms.DTO.Response.TherapyPlanDTO;
import com.therapistms.ENUM.TherapistDecisionStatus;
import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.Entity.Therapist;
import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Repository.TherapyPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapyPlanService {

	private final TherapyPlanRepository therapyPlanRepository;
	private final TherapistRepository therapistRepository;
	private final PatientClient patientClient;

	public boolean createTherapyPlan(TherapyPlanRequest request) {
		log.info("Got the request to make therapist plan : {}", request);
		boolean therapyPlan = therapyPlanRepository.existsByMedicalRecordIdAndTherapistId(request.getMedicalRecordId(), request.getTherapistId());
		if (therapyPlan) {
			return true;
		}
		Therapist therapist = therapistRepository.findTherapistByUserId(request.getTherapistId());
		if (therapist == null) {
			log.warn("No therapist found with id {}", request.getTherapistId());
			return false;
		}
		boolean medicalRecordCheck = patientClient.checkMedicalRecordByMedicalId(request.getMedicalRecordId());
		if (!medicalRecordCheck) {
			log.warn("No medical record found with id {}", request.getMedicalRecordId());
			return false;
		}
		TherapyPlan plan = TherapyPlan.builder()
				.medicalRecordId(request.getMedicalRecordId())
				.completedSessions(0)
				.therapistDecisionStatus(TherapistDecisionStatus.PENDING)
				.status(TherapyPlanStatus.NOT_ASSIGNED)
				.therapistId(therapist.getUserId())
				.therapistName(therapist.getTherapistName())
				.build();

		TherapyPlan saved = therapyPlanRepository.save(plan);
		return true;
	}


	public void updateTherapistDecision(
			String therapyPlanId,
			TherapistDecisionStatus decisionStatus
	) {
		TherapyPlan plan = therapyPlanRepository.findTherapyPlanByTherapyPlanId(therapyPlanId).orElse(null);
		if (plan == null) {
			log.info("NO Therapy plan found");
			return;
		}
		if (plan.getTherapistDecisionStatus() != TherapistDecisionStatus.PENDING) {
			throw new IllegalStateException("Decision already taken");
		}

		plan.setTherapistDecisionStatus(decisionStatus);

		if (decisionStatus == TherapistDecisionStatus.APPROVED) {
			plan.setStatus(TherapyPlanStatus.ASSIGNED);
		}
		if(decisionStatus == TherapistDecisionStatus.REJECTED){
			MedicalRecordUpdateRequest  medicalRecordUpdateRequest = MedicalRecordUpdateRequest.builder()
					.needTherapy(false)
					.build();
//			Boolean saveToMedicalRecord = patientClient.updateMedicalRecord(plan.getMedicalRecordId(),medicalRecordUpdateRequest);
			deleteTherapyPlan(therapyPlanId);
		} else therapyPlanRepository.save(plan);
	}

	public void startTherapy(String therapyPlanId) {
		TherapyPlan plan = therapyPlanRepository.findTherapyPlanByTherapyPlanId(therapyPlanId).orElse(null);
		if (plan == null) {
			log.info("NO Therapy plan");
			return;
		}
		if (plan.getTherapistDecisionStatus() != TherapistDecisionStatus.APPROVED) {
			throw new IllegalStateException("Therapy not approved by therapist");
		}

		if (plan.getStatus() != TherapyPlanStatus.ASSIGNED) {
			throw new IllegalStateException("Therapy cannot be started");
		}

		plan.setStatus(TherapyPlanStatus.ACTIVE);
		therapyPlanRepository.save(plan);
	}

	public void completeSession(String therapyPlanId) {
		TherapyPlan plan = therapyPlanRepository.findTherapyPlanByTherapyPlanId(therapyPlanId).orElse(null);
		if (plan == null) {
			return;
		}
		if (plan.getStatus() != TherapyPlanStatus.ACTIVE) {
			throw new IllegalStateException("Therapy is not active");
		}

		plan.setCompletedSessions(plan.getCompletedSessions() + 1);

		if (plan.getCompletedSessions() >= plan.getTotalSessions()) {
			plan.setStatus(TherapyPlanStatus.COMPLETED);
		}

		therapyPlanRepository.save(plan);
	}


	public TherapyPlanDTO getTherapyPlanById(String therapyPlanId) {
		TherapyPlan plan = therapyPlanRepository.findById(therapyPlanId)
				.orElseThrow(() -> new RuntimeException("Therapy Plan not found"));

		return mapToResponse(plan);
	}

	public TherapyPlanDTO mapToResponse(TherapyPlan plan) {
		return TherapyPlanDTO.builder()
				.therapyPlanId(plan.getTherapyPlanId())
				.therapyType(plan.getTherapyType())
				.totalSessions(plan.getTotalSessions())
				.completedSessions(plan.getCompletedSessions())
				.sessionDurationMinutes(plan.getSessionDurationMinutes())
				.therapistDecisionStatus(plan.getTherapistDecisionStatus())
				.status(plan.getStatus())
				.frequency(plan.getFrequency())
				.startDate(plan.getStartDate())
				.status(plan.getStatus())
				.therapistId(plan.getTherapistId())
				.therapistName(plan.getTherapistName())
				.therapistNotes(plan.getTherapistNotes())
				.build();
	}


	public List<String> getMedicalIdsByTherapistId(String therapistId) {
		List<TherapyPlan> therapyPlans =
				therapyPlanRepository.findAllByTherapistId(therapistId);
		List<String> medicalIds = new ArrayList<>();
		for (TherapyPlan plan : therapyPlans) {
			medicalIds.add(plan.getMedicalRecordId());
		}
		return medicalIds;
	}


	public List<TherapyPlanDTO> getTherapyPlansByTherapistId(
			String therapistId
	) {
		List<TherapyPlan> plans =
				therapyPlanRepository.findAllByTherapistId(therapistId);

		return plans.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public void deleteTherapyPlan(String planId){
		TherapyPlan therapyPlan = therapyPlanRepository.findTherapyPlansByTherapyPlanId(planId);
		therapyPlanRepository.delete(therapyPlan);
	}

}