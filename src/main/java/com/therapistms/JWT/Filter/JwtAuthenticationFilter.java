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

		log.info(
				"Incoming request intercepted by JwtAuthenticationFilter: {}",
				request.getRequestURI()
		);

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			log.info(
					"No Authorization header is : {} found for request: {}",header,
					request.getRequestURI()
			);
			filterChain.doFilter(request, response);
			return;
		}


		log.info(
				"Authorization header is : {} found for request: {}",header,
				request.getRequestURI()
		);

		String token = header.substring(7);

		try {
			Claims claims = jwtUtil.getClaims(token);
			String subject = claims.getSubject();

			if (jwtUtil.isServiceToken(claims)) {

				log.info(
						"Authenticated SERVICE token | Subject: {} | Allowing request: {}",
						subject,
						request.getRequestURI()
				);

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								subject,
								null,
								List.of(() -> "ROLE_SERVICE")
						);

				SecurityContextHolder
						.getContext()
						.setAuthentication(authentication);

			} else {

				String role = claims.get("role", String.class);

				if (role == null) {
					log.error(
							"JWT missing role claim | Rejecting request: {}",
							request.getRequestURI()
					);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				log.info(
						"Authenticated USER token | Subject: {} | Role: ROLE_{} | Allowing request: {}",
						subject,
						role,
						request.getRequestURI()
				);

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								subject,
								null,
								List.of(() -> "ROLE_" + role)
						);

				SecurityContextHolder
						.getContext()
						.setAuthentication(authentication);
			}

		} catch (Exception ex) {
			log.error(
					"JWT validation failed | Rejecting request: {}",
					request.getRequestURI(),
					ex
			);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		log.debug(
				"Request {} passed JwtAuthenticationFilter successfully",
				request.getRequestURI()
		);

		filterChain.doFilter(request, response);
	}
}
