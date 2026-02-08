package com.therapistms.Controller;

import com.therapistms.Entity.TherapySession;
import com.therapistms.Service.TherapyPlanService;
import com.therapistms.Service.TherapySessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/therapists/therapy-sessions")
@RequiredArgsConstructor
public class TherapySessionController {

	private final TherapySessionService sessionService;
	private final TherapyPlanService therapyPlanService;

	@PostMapping("/book")
	public ResponseEntity<?> bookSession(
			@RequestParam String slotId,
			@RequestParam String therapyPlanId,
			@RequestParam String medicalRecordId
	) {
		log.info("Got the request to book therapy date");
		sessionService.bookSlot(slotId, therapyPlanId,medicalRecordId);
		return ResponseEntity.ok("Session booked");
	}

	@GetMapping("/plan/{planId}")
	public ResponseEntity<List<TherapySession>> getMySessions(@PathVariable String planId) {
		return ResponseEntity.ok(sessionService.getSessionsByPlan(planId));
	}

	@DeleteMapping("/{sessionId}")
	public ResponseEntity<?> cancelSession(@PathVariable String sessionId) {
		log.info("Request to cancel therapy session: {}", sessionId);
		sessionService.cancelSession(sessionId);
		return ResponseEntity.ok("Session cancelled and slot released");
	}

	@GetMapping("/therapist/{therapistId}")
	public ResponseEntity<List<TherapySession>> getTherapistSessions(@PathVariable String therapistId) {
		return ResponseEntity.ok(sessionService.getSessionsByTherapist(therapistId));
	}


	@PutMapping("/{sessionId}/session/complete")
	public ResponseEntity<Boolean> completeSession(@PathVariable String sessionId) {
		log.info("Marking session {} as completed", sessionId);
		boolean success = therapyPlanService.completeSession(sessionId);
		return ResponseEntity.ok(success);
	}
}
