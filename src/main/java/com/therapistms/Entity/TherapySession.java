package com.therapistms.Entity;

import com.therapistms.ENUM.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "therapy_sessions",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"slot_id"})
		})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TherapySession {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String sessionId;

	private String therapyPlanId;
	private String therapistId;
	private String medicalRecordId;

	private String slotId;

	private LocalDate sessionDate;
	private LocalTime startTime;
	private LocalTime endTime;

	private SlotStatus status;
}
