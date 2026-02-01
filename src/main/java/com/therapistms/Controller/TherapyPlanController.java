package com.therapistms.Controller;


import com.therapistms.DTO.Request.TherapyPlanRequest;
import com.therapistms.DTO.Response.TherapyPlanDTO;
import com.therapistms.ENUM.TherapistDecisionStatus;
import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Repository.TherapyPlanRepository;
import com.therapistms.Service.TherapyPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/therapists/therapy-plans")
@RequiredArgsConstructor
public class TherapyPlanController {

	private final TherapyPlanService therapyPlanService;
	private final TherapyPlanRepository therapyPlanRepository;


	@GetMapping
	public ResponseEntity<List<TherapyPlan>> getTherapyPlan() {
		List<TherapyPlan> response =
				therapyPlanRepository.findAll();
				return ResponseEntity.ok(response);
	}

	@GetMapping("/{therapyPlanId}")
	public ResponseEntity<TherapyPlanDTO> getTherapyPlanById(
			@PathVariable String therapyPlanId) {

		TherapyPlanDTO response =
				therapyPlanService.getTherapyPlanById(therapyPlanId);
		if(response==null){
			log.warn("TherapyPlanResponse not found for Id={}", therapyPlanId);
			return ResponseEntity.ok(null);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/therapist/{therapistId}")
	public ResponseEntity<List<TherapyPlanDTO>> getTherapyPlansByTherapistId(
			@PathVariable String therapistId
	) {
		log.info("Fetching therapy plans for therapistId={}", therapistId);

		List<TherapyPlanDTO> response =
				therapyPlanService.getTherapyPlansByTherapistId(therapistId);

		return ResponseEntity.ok(response);
	}


	@GetMapping("/medical-record/{medicalRecordId}")
	public ResponseEntity<TherapyPlanDTO> getTherapyPlanByMedicalRecordId(
			@PathVariable String medicalRecordId) {

		TherapyPlan therapyPlan =  therapyPlanRepository.findTherapyPlansByMedicalRecordId(medicalRecordId);
		if(therapyPlan==null){
			log.warn("No Therapy plan found for medicalRecordId = {}", medicalRecordId);
		    return ResponseEntity.ok(null);
		}
		return ResponseEntity.ok(therapyPlanService.mapToResponse(therapyPlan));
	}

	@PostMapping
	public ResponseEntity<Boolean> createTherapyPlan(
			@RequestBody TherapyPlanRequest request) {
		log.info("creating Therapy Plan");
		boolean response =
				therapyPlanService.createTherapyPlan(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}




	@PutMapping("/{id}/decision")
	public void updateDecision(
			@PathVariable String id,
			@RequestParam TherapistDecisionStatus status
	) {
		therapyPlanService.updateTherapistDecision(id, status);
	}

	@PutMapping("/{id}/start")
	public void startTherapy(@PathVariable String id) {
		therapyPlanService.startTherapy(id);
	}

	@PutMapping("/{id}/session/complete")
	public void completeSession(@PathVariable String id) {
		therapyPlanService.completeSession(id);
	}


}
