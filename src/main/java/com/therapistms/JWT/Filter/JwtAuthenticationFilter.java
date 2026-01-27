package com.therapistms.JWT.Filter;

import com.therapistms.JWT.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {

		log.info("Accepted By Jwt Filter in TherapistMS request: {}", request.getRequestURI());

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			log.info("No Header");
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);

		try {
			Claims claims = jwtUtil.getClaims(token);
			String subject = claims.getSubject();

			if (jwtUtil.isServiceToken(claims)) {
				log.info("Identified as Service token from: {}", subject);

				UsernamePasswordAuthenticationToken auth =
						new UsernamePasswordAuthenticationToken(
								subject,
								null,
								List.of(() -> "ROLE_SERVICE")
						);

				log.info("We set this request :{} as role : ROLE_SERVICE",request.getRequestURI());
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				log.info("User token for userId/email: {}", subject);
				String role = claims.get("role", String.class);

				if (role == null) {
					throw new RuntimeException("Role missing in JWT");
				}

				UsernamePasswordAuthenticationToken auth =
						new UsernamePasswordAuthenticationToken(
								subject,
								null,
								List.of(() -> "ROLE_" + role)
						);

				log.info(
						"We set this request : {} as role : ROLE_{}",
						request.getRequestURI(),
						role
				);

				SecurityContextHolder.getContext().setAuthentication(auth);

			}
		} catch (Exception e) {
			log.error("Invalid JWT Declared by Therapist Service", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		filterChain.doFilter(request, response);
	}
}
