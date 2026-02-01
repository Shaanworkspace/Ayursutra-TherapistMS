package com.therapistms.Controller;

import com.therapistms.Entity.Scheduling.TherapistSlot;
import com.therapistms.Service.TherapistSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/therapists/slots")
@RequiredArgsConstructor
public class TherapistSlotController {

	private final TherapistSlotService slotService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllSlots(Authentication authentication) {
		String email = (String) authentication.getPrincipal();
		return ResponseEntity.ok(
				slotService.getAllSlotsByTherapist(email)
		);
	}

	@GetMapping("/therapist/{therapistId}")
	public List<TherapistSlot> getAllTherapistSlotByTherapistId(@PathVariable String therapistId){
		return slotService.getAllSlotsByTherapist(therapistId);
	}

	@GetMapping("/available-slots")
	public ResponseEntity<List<TherapistSlot>> getAvailableSlots(
			@RequestParam String therapistId,
			@RequestParam String from,
			@RequestParam String to
	) {
		return ResponseEntity.ok(
				slotService.getAvailableSlotsInRange(
						therapistId,
						LocalDate.parse(from),
						LocalDate.parse(to)
				)
		);
	}


	@GetMapping
	public ResponseEntity<?> getSlotsForDay(
			@RequestParam String date,
			Authentication authentication
	) {
		String therapistEmail = (String) authentication.getPrincipal();
		return ResponseEntity.ok(
				slotService.getSlotsForTherapistByDate(
						therapistEmail,
						LocalDate.parse(date)
				)
		);
	}


	// Delete one slot
	@DeleteMapping("/{slotId}")
	public ResponseEntity<?> deleteSlot(@PathVariable String slotId) {
		slotService.deleteSlot(slotId);
		return ResponseEntity.ok("Slot deleted");
	}

	// Clear full day (holiday)
	@DeleteMapping("/clear-day")
	public ResponseEntity<?> clearDay(
			@RequestParam String therapistId,
			@RequestParam String date
	) {
		slotService.clearDaySlots(therapistId, LocalDate.parse(date));
		return ResponseEntity.ok("Day cleared");
	}

	// Add extra slot
	@PostMapping("/add")
	public ResponseEntity<?> addExtraSlot(
			@RequestParam String date,
			@RequestParam String startTime,
			@RequestParam String endTime,
			Authentication authentication
	) {
		if (startTime.isBlank() || endTime.isBlank()) {
			throw new IllegalArgumentException("Start time and end time are required");
		}

		String email = (String) authentication.getPrincipal();

		return ResponseEntity.ok(
				slotService.addExtraSlotByEmail(
						email,
						LocalDate.parse(date),
						LocalTime.parse(startTime),
						LocalTime.parse(endTime)
				)
		);
	}

}
