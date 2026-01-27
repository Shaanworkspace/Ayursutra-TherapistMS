package com.therapistms.Entity.Scheduling;

import com.therapistms.ENUM.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
		name = "therapist_slot",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"therapist_id", "slot_date", "start_time"})
		}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistSlot {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String slotId;

	@Column(name = "therapist_id", nullable = false)
	private String therapistId;

	@Column(name = "slot_date", nullable = false)
	private LocalDate slotDate;

	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;

	@Enumerated(EnumType.STRING)
	private SlotStatus status; 

	private String lockedByPatientId;
	private LocalDateTime lockExpiryTime;
}
