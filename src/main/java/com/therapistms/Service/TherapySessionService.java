package com.therapistms.Service;


import com.therapistms.Client.PatientClient;
import com.therapistms.ENUM.SlotStatus;
import com.therapistms.Entity.Scheduling.TherapistSlot;
import com.therapistms.Entity.TherapySession;
import com.therapistms.Repository.TherapistSlotRepository;
import com.therapistms.Repository.TherapySessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapySessionService {

	private final TherapistSlotRepository slotRepository;
	private final TherapySessionRepository sessionRepository;
	private final PatientClient patientClient;

	@Transactional
	public void bookSlot(
			String slotId,
			String therapyPlanId
	) {
		log.info("Inside Book Slot method");
		TherapistSlot slot = slotRepository.findById(slotId)
				.orElseThrow(() -> new RuntimeException("Slot not found"));

		if (slot.getStatus() != SlotStatus.AVAILABLE) {
			throw new RuntimeException("Slot already booked");
		}

		if (sessionRepository.existsBySlotId(slotId)) {
			throw new RuntimeException("Session already exists");
		}

		TherapySession session = TherapySession.builder()
				.slotId(slotId)
				.therapyPlanId(therapyPlanId)
				.therapistId(slot.getTherapistId())
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
}
