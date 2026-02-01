package com.therapistms.Repository;

import com.therapistms.Entity.TherapySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapySessionRepository
		extends JpaRepository<TherapySession, String> {

	boolean existsBySlotId(String slotId);
}