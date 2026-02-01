package com.therapistms.Client.Fallback;

import com.therapistms.Client.DoctorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorClientFallback implements DoctorClient {
	@Override
	public boolean checkDoctorByUserId(String userId) {
		log.warn("Falling back from Therapist MS as NO Doc found by User Id : {} in Doc Microservice ",userId);
		return false;
	}

}
