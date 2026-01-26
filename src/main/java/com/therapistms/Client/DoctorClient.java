package com.therapistms.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name ="doctor-service",
		url = "${services.doctor.url}"
)
public interface DoctorClient {
	@GetMapping("/api/doctors/check/{userId}")
	boolean checkDoctorByUserId(@PathVariable String userId);
}
