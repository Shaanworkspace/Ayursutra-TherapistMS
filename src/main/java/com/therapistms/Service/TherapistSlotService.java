package com.therapistms.Service;

import com.therapistms.ENUM.SlotStatus;
import com.therapistms.Entity.Scheduling.TherapistSlot;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Repository.TherapistSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TherapistSlotService {

	private final TherapistSlotRepository slotRepository;
	private final TherapistRepository therapistRepository;

	public List<TherapistSlot> getAllSlotsByTherapist(String email) {
		Therapist therapist = therapistRepository.findByEmail(email);
		if (therapist == null) {
			throw new RuntimeException("Therapist not found");
		}

		return slotRepository.findByTherapistId(therapist.getUserId());
	}

	public List<TherapistSlot> getSlotsForTherapistByDate(
			String therapistEmail,
			LocalDate date
	) {
		Therapist therapist = therapistRepository.findByEmail(therapistEmail);
		if (therapist == null) {
			throw new RuntimeException("Therapist not found");
		}

		return slotRepository.findByTherapistIdAndSlotDate(
				therapist.getUserId(),
				date
		);
	}


	public void deleteSlot(String slotId) {
		slotRepository.deleteById(slotId);
	}

	public void clearDaySlots(String therapistId, LocalDate date) {
		slotRepository.deleteByTherapistIdAndSlotDate(therapistId, date);
	}

	public TherapistSlot addExtraSlot(
			String therapistId,
			LocalDate date,
			LocalTime startTime,
			LocalTime endTime
	) {
		TherapistSlot slot = TherapistSlot.builder()
				.therapistId(therapistId)
				.slotDate(date)
				.startTime(startTime)
				.endTime(endTime)
				.status(SlotStatus.AVAILABLE)
				.build();

		return slotRepository.save(slot);
	}


	public List<TherapistSlot> generateSlots(
			String therapistId,
			LocalDate date
	) {

		List<TherapistSlot> slots = new ArrayList<>();

		LocalTime start = LocalTime.of(8, 0);

		for (int i = 0; i < 8; i++) {

			LocalTime end = start.plusMinutes(60);

			TherapistSlot slot = TherapistSlot.builder()
					.therapistId(therapistId)
					.slotDate(date)
					.startTime(start)
					.endTime(end)
					.status(SlotStatus.AVAILABLE)
					.build();

			slots.add(slot);

			start = end.plusMinutes(30); // break
		}
		return slots;
	}
	public TherapistSlot addExtraSlotByEmail(
			String email,
			LocalDate date,
			LocalTime startTime,
			LocalTime endTime
	) {
		Therapist therapist = therapistRepository.findByEmail(email);
		if (therapist == null) {
			throw new RuntimeException("Therapist not found for email: " + email);
		}

		// Optional but recommended: prevent overlapping slots
		boolean exists = slotRepository.existsByTherapistIdAndSlotDateAndStartTime(
				therapist.getUserId(),
				date,
				startTime
		);

		if (exists) {
			throw new RuntimeException("Slot already exists for this time");
		}

		TherapistSlot slot = TherapistSlot.builder()
				.therapistId(therapist.getUserId())
				.slotDate(date)
				.startTime(startTime)
				.endTime(endTime)
				.status(SlotStatus.AVAILABLE)
				.build();

		return slotRepository.save(slot);
	}

}

