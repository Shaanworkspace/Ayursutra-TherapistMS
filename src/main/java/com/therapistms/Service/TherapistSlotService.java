package com.therapistms.Service;

import com.therapistms.ENUM.SlotStatus;
import com.therapistms.Entity.Scheduling.TherapistSlot;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Repository.TherapistSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TherapistSlotService {

	private final TherapistSlotRepository slotRepository;
	private final TherapistRepository therapistRepository;

	public List<TherapistSlot> getAllSlotsByTherapist(String email) {
		Therapist therapist = therapistRepository.findTherapistByEmail(email);
		if (therapist == null) {
			log.info("Therapist not found with email id : {}",email);
			return List.of();
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
		List<TherapistSlot> therapistSlots = slotRepository.findByTherapistIdAndSlotDate(
				therapist.getUserId(),
				date
		);
		if(therapistSlots.isEmpty()){
			log.info("No Therapist Slot fount with therapistEmail : {}",therapistEmail);
			return List.of();
		}
		return therapistSlots;
	}



	public void initDefaultSlots(String therapistId) {
		LocalDate today = LocalDate.now();

		for (int i = 0; i < 7; i++) {
			LocalDate targetDate = today.plusDays(i);

			List<TherapistSlot> existing = slotRepository.findByTherapistIdAndSlotDate(therapistId, targetDate);

			if (existing.isEmpty()) {
				List<TherapistSlot> defaultSlots = generateSlots(therapistId, targetDate);
				slotRepository.saveAll(defaultSlots);
				log.info("Generated default slots for therapist: {} on date: {}", therapistId, targetDate);
			}
		}
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

	public List<TherapistSlot> generateSlots(String therapistId, LocalDate date) {
		List<TherapistSlot> slots = new ArrayList<>();
		LocalTime start = LocalTime.of(9, 0);

		for (int i = 0; i < 6; i++) {
			LocalTime end = start.plusMinutes(60);

			slots.add(TherapistSlot.builder()
					.therapistId(therapistId)
					.slotDate(date)
					.startTime(start)
					.endTime(end)
					.status(SlotStatus.AVAILABLE)
					.build());

			start = end.plusMinutes(30);
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

	private boolean isDateInRange(LocalDate date, LocalDate from, LocalDate to) {
		return (date.isEqual(from) || date.isAfter(from)) &&
				(date.isEqual(to)   || date.isBefore(to));
	}

	public List<TherapistSlot> getAvailableSlotsInRange(String therapistId, LocalDate from,
	                                                    LocalDate to) {
		if (from.isAfter(to)) {
			throw new IllegalArgumentException("From date cannot be after To date");
		}

		List<TherapistSlot> therapistSlots =
				slotRepository.findByTherapistId(therapistId);

		List<TherapistSlot> slotsInRange = therapistSlots.stream()
				.filter(slot -> isDateInRange(slot.getSlotDate(), from, to))
				.toList();

		return slotsInRange;

	}
}

