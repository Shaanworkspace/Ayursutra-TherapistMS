package com.therapistms.Entity;


import com.therapistms.DTO.Response.MedicalRecord;
import com.therapistms.ENUM.Status;
import com.therapistms.ENUM.TherapistDecisionStatus;
import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.ENUM.TherapyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "therapy_plans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TherapyPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, updatable = false)
	private String therapyPlanId;

	private String medicalRecordId;

	@Enumerated(EnumType.STRING)
	private TherapyType therapyType;

	private int totalSessions;
	private int completedSessions;

	private int sessionDurationMinutes;   // 30, 45, 60
	private String frequency;              // DAILY / WEEKLY

	private LocalDate startDate;
	private LocalDate createdDate;
	private TherapistDecisionStatus therapistDecisionStatus;
	private TherapyPlanStatus status;


	private String therapistId;
	private String therapistName;
	private String therapistNotes;

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDate.now();
	}
}
