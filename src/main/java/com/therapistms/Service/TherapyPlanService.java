package com.therapistms.Service;

import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.MedicalRecordUpdateRequest;
import com.therapistms.DTO.Request.TherapyPlanRequest;
import com.therapistms.DTO.Response.TherapyPlanDTO;
import com.therapistms.ENUM.MedicalRecordStatus;
import com.therapistms.ENUM.SlotStatus;
import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.Entity.Therapist;
import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Entity.TherapySession;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Repository.TherapyPlanRepository;
import com.therapistms.Repository.TherapySessionRepository;
import jakarta.transaction.Transactional;
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
	private  final TherapySessionRepository sessionRepository;

	public boolean createTherapyPlan(TherapyPlanRequest request) {
		log.info("Got the request to make therapist plan : {}", request);

		//Assure that One patient can have Only One therapy Plan
		TherapyPlan therapyPlan = therapyPlanRepository.findByMedicalRecordIdAndTherapistId(request.getMedicalRecordId(), request.getTherapistId());
		if (therapyPlan!=null) {
			log.info("Therapy plan already exist with id:{}",therapyPlan.getTherapyPlanId());
			return true;
		}

		//Check Therapist Exist or not
		Therapist therapist = therapistRepository.findTherapistByUserId(request.getTherapistId());
		if (therapist ==null) {
			log.warn("No therapist found with id {}", request.getTherapistId());
			return false;
		}
		log.info("Got Therapist : {}",therapist);

		//Check and Update Status of medical Record
		boolean updated = updateMedicalRecordStatus(request.getMedicalRecordId(), "WAITING_FOR_THERAPIST_APPROVAL");
		if (!updated) {
			log.warn("Failed to update medical record [{}] while starting therapy", request.getMedicalRecordId());
		}

		TherapyPlan plan = TherapyPlan.builder()
				.medicalRecordId(request.getMedicalRecordId())
				.status(TherapyPlanStatus.ASSIGNED)
				.totalTherapySessions(request.getTotalTherapySessions())
				.therapistId(therapist.getUserId())
				.therapistName(therapist.getTherapistName())
				.build();

		therapyPlanRepository.save(plan);
		return true;
	}

	public void startTherapy(String therapyPlanId) {
		TherapyPlan plan = therapyPlanRepository.findTherapyPlanByTherapyPlanId(therapyPlanId).orElseThrow(() ->new RuntimeException("No Therapy plan exist with id: "+therapyPlanId));
		log.info("Got Plan : [ {} ]",plan);
		plan.setStatus(TherapyPlanStatus.ACTIVE);
		boolean updated = updateMedicalRecordStatus(plan.getMedicalRecordId(), "THERAPIST_APPROVED");
		if (!updated) {
			log.warn("Failed to update medical record [{}] while starting therapy", plan.getMedicalRecordId());
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
				.therapies(plan.getTherapies())
				.totalTherapySessions(plan.getTotalTherapySessions())
				.bookedTherapySessions(plan.getBookedTherapySessions())
				.completedTherapySessions(plan.getCompletedTherapySessions())
				.sessionDurationMinutes(plan.getSessionDurationMinutes())
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


	public boolean updateMedicalRecordStatus(String medicalRecordId, String status) {
		try {
			MedicalRecordUpdateRequest request = MedicalRecordUpdateRequest.builder()
					.sessionMedicalRecordStatus(MedicalRecordStatus.valueOf(status))
					.build();

			log.info("Updating Medical Record [{}] with status [{}]", medicalRecordId, status);
			return patientClient.updateMedicalRecord(medicalRecordId, request);
		} catch (IllegalArgumentException e) {
			log.error("Invalid MedicalRecordStatus [{}] provided", status, e);
			return false;
		} catch (Exception e) {
			log.error("Failed to update medical record [{}]", medicalRecordId, e);
			return false;
		}
	}

	public boolean diagnosedByTherapist(String medicalRecordId, TherapyPlanRequest request) {
		TherapyPlan plan = therapyPlanRepository.findTherapyPlansByMedicalRecordId(medicalRecordId).orElseThrow(()->new RuntimeException("No Therapy Plan with medical Id: "+medicalRecordId));

		plan.setTherapies(request.getTherapies());
		plan.setTherapistNotes(request.getTherapistNotes());
		plan.setStatus(TherapyPlanStatus.ACTIVE);
		plan.setTotalTherapySessions(request.getTotalTherapySessions());

		boolean patientStatusUpdated = updateMedicalRecordStatus(medicalRecordId, "THERAPY_IN_PROGRESS");

		if (!patientStatusUpdated) {
			log.warn("Therapy plan updated locally, but failed to update Patient Microservice status");
		}
		therapyPlanRepository.save(plan);

		log.info("Therapy plan for record {} is now ACTIVE with selected therapies", medicalRecordId);
		return true;
	}



	@Transactional
	public boolean completeSession(String sessionId) {
		TherapySession session = sessionRepository.findById(sessionId)
				.orElseThrow(() -> new RuntimeException("Session not found"));

		if (session.getStatus() == SlotStatus.COMPLETED) return true;

		// 1. Session status badlo
		session.setStatus(SlotStatus.COMPLETED);
		sessionRepository.save(session);

		// 2. Plan update logic
		TherapyPlan plan = therapyPlanRepository.findById(session.getTherapyPlanId())
				.orElseThrow(() -> new RuntimeException("Plan not found"));

		// Booked kam karo aur Completed badhao
		plan.setBookedTherapySessions(Math.max(0, plan.getBookedTherapySessions() - 1));
		plan.setCompletedTherapySessions(plan.getCompletedTherapySessions() + 1);

		// Agar saare sessions ho gaye toh status COMPLETED kar do
		if (plan.getCompletedTherapySessions() >= plan.getTotalTherapySessions()) {
			plan.setStatus(TherapyPlanStatus.COMPLETED);
			updateMedicalRecordStatus(session.getMedicalRecordId(),"COMPLETED");
			log.info("Marked as Completed");
		}

		therapyPlanRepository.save(plan);
		return true;
	}
}