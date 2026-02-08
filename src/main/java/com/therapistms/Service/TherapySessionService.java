package com.therapistms.Service;


import com.therapistms.Client.PatientClient;
import com.therapistms.ENUM.SlotStatus;
import com.therapistms.Entity.Scheduling.TherapistSlot;
import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Entity.TherapySession;
import com.therapistms.Repository.TherapistSlotRepository;
import com.therapistms.Repository.TherapyPlanRepository;
import com.therapistms.Repository.TherapySessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapySessionService {

	private final TherapistSlotRepository slotRepository;
	private final TherapySessionRepository sessionRepository;
	private final TherapyPlanRepository therapyPlanRepository;

	@Transactional
	public void bookSlot(
			String slotId,
			String therapyPlanId,
			String medicalRecordId) {
		log.info("Inside Book Slot method");
		TherapistSlot slot = slotRepository.findById(slotId)
				.orElseThrow(() -> new RuntimeException("Slot not found"));

		if (slot.getStatus() != SlotStatus.AVAILABLE) {
			throw new RuntimeException("Slot already booked");
		}

		TherapyPlan plan = therapyPlanRepository.findById(therapyPlanId)
				.orElseThrow(() -> new RuntimeException("Therapy Plan not found"));
		if (plan.getBookedTherapySessions() + 1 > plan.getTotalTherapySessions()) {
			throw new RuntimeException("Please STOP ! Maximum session limit reached.");
		}

		plan.setBookedTherapySessions(plan.getBookedTherapySessions()+ 1);
		therapyPlanRepository.save(plan);

		log.info("Incremented total sessions for plan: {}. New Total: {}",
				therapyPlanId, plan.getTotalTherapySessions());


		if (sessionRepository.existsBySlotId(slotId)) {
			throw new RuntimeException("Session already exists");
		}

		TherapySession session = TherapySession.builder()
				.slotId(slotId)
				.therapyPlanId(therapyPlanId)
				.therapistId(slot.getTherapistId())
				.medicalRecordId(medicalRecordId)
				.sessionDate(slot.getSlotDate())
				.startTime(slot.getStartTime())
				.endTime(slot.getEndTime())
				.status(SlotStatus.BOOKED)
				.build();

		log.info("Saving the session : {}",session);
		sessionRepository.save(session);

		slot.setStatus(SlotStatus.BOOKED);

		slotRepository.save(slot);
	}

	public List<TherapySession> getSessionsByPlan(String planId) {
		return sessionRepository.findAllByTherapyPlanId(planId);
	}

	@Transactional
	public void cancelSession(String sessionId) {

		TherapySession session = sessionRepository.findById(sessionId)
				.orElseThrow(() -> new RuntimeException("Session not found"));

		TherapyPlan plan = therapyPlanRepository.findById(session.getTherapyPlanId()).orElseThrow(()-> new RuntimeException("No therapy plan found"));
		plan.setTotalTherapySessions(plan.getTotalTherapySessions() - 1);
		therapyPlanRepository.save(plan);

		TherapistSlot slot = slotRepository.findById(session.getSlotId())
				.orElseThrow(() -> new RuntimeException("Associated slot not found"));

		slot.setStatus(SlotStatus.AVAILABLE);
		slotRepository.save(slot);

		sessionRepository.delete(session);
		log.info("Session {} cancelled. Slot {} is now AVAILABLE.", sessionId, slot.getSlotId());
	}

	public List<TherapySession> getSessionsByTherapist(String therapistId) {
		List<TherapySession> allByTherapistId = sessionRepository.findAllByTherapistId(therapistId);
		if(allByTherapistId==null){
			log.info("NO Therapy session");
			return List.of();
		}
		else return allByTherapistId;
	}
}
