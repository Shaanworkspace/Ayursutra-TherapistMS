package com.therapistms.Entity;

import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.ENUM.TherapyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapy_plans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TherapyPlan {  // One plan can have many therapist Session
	/*
	Patient: Rahul
	Problem: shoulder pain
	Doctor says: 10 sessions required -> so it have many sessions
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, updatable = false)
	private String therapyPlanId;

	private String medicalRecordId;

	@Enumerated(EnumType.STRING)
	private List<TherapyType> therapies = new ArrayList<>();

	private int totalTherapySessions = 0;     // Limit (e.g., 10)
	private int bookedTherapySessions = 0;    // Currently scheduled (upcoming)
	private int completedTherapySessions = 0; // Sessions already done

	private int sessionDurationMinutes;   // 30, 45, 60
	private String frequency;              // DAILY / WEEKLY

	private LocalDate startDate;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@UpdateTimestamp
	private LocalDateTime updateDateAndTime;

	private TherapyPlanStatus status;

	private String therapistId;
	private String therapistName;
	private String therapistNotes;


}
