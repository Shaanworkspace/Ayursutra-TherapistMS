package com.therapistms.Repository;

import com.therapistms.ENUM.SlotStatus;
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

	List<TherapistSlot> findByTherapistIdAndStatusAndSlotDateBetweenOrderBySlotDateAscStartTimeAsc(String therapistId, SlotStatus status, LocalDate slotDateAfter, LocalDate slotDateBefore);
	List<TherapistSlot> findByTherapistId(String therapistId);
	List<TherapistSlot> findBySlotDateBetween(
			LocalDate from,
			LocalDate to
	);


}