package com.therapistms.Repository;

import com.therapistms.Entity.Scheduling.TherapistSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TherapistSlotRepository extends JpaRepository<TherapistSlot, String> {
	List<TherapistSlot> findByTherapistIdAndSlotDate(String therapistId, LocalDate slotDate);
	void deleteByTherapistIdAndSlotDate(String therapistId, LocalDate slotDate);

	boolean existsByTherapistIdAndSlotDateAndStartTime(String therapistId, LocalDate slotDate, LocalTime startTime);
	List<TherapistSlot> findByTherapistId(String therapistId);
}