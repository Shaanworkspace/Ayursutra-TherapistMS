package com.therapistms.Client.Interceptor;

import com.therapistms.JWT.JwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {
	private final JwtUtil jwtUtil;
	@Value("${spring.application.name}")
	private String serviceName;


	@Override
	public void apply(RequestTemplate template) {
		log.info("Entered in Therapist Interceptor");
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		if (attributes == null) {
			log.warn("FeignAuthInterceptor: No ServletRequestAttributes found");
			return;
		}

		HttpServletRequest request = attributes.getRequest();
		String authHeader = request.getHeader("Authorization");
		// Case 1: User request present → forward user JWT
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			log.info("Feign: Forwarding Therapist token");
			template.header("Authorization", authHeader);
			return;
		}

		// Case 2: No user context → generate SERVICE token
		String serviceToken = jwtUtil.generateServiceToServiceToken(serviceName);

		log.info("Feign: Using SERVICE token for {}", serviceName);

		template.header("Authorization", "Bearer " + serviceToken);
	}
}
