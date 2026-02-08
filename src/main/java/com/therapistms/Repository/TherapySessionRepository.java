package com.therapistms.Repository;

import com.therapistms.Entity.TherapySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapySessionRepository
		extends JpaRepository<TherapySession, String> {

	boolean existsBySlotId(String slotId);
	List<TherapySession> findAllByTherapyPlanId(String therapyPlanId);

	void deleteBySessionId(String sessionId);

	List<TherapySession> findAllByTherapistId(String therapistId);
}