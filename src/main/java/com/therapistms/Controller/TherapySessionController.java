package com.therapistms.Controller;

import com.therapistms.Service.TherapySessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/therapists/therapy-sessions")
@RequiredArgsConstructor
public class TherapySessionController {

	private final TherapySessionService sessionService;

	@PostMapping("/book")
	public ResponseEntity<?> bookSession(
			@RequestParam String slotId,
			@RequestParam String therapyPlanId
	) {
		log.info("Got the request to book therapy date");
		sessionService.bookSlot(slotId, therapyPlanId);
		return ResponseEntity.ok("Session booked");
	}
}
