package com.therapistms.DTO.Request;
import com.therapistms.ENUM.TherapistDecisionStatus;
import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.ENUM.TherapyType;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyPlanRequest {

	private String medicalRecordId;

	private TherapyType therapyType;
	private TherapistDecisionStatus therapistDecisionStatus;
	private TherapyPlanStatus status;
	private int totalSessions;
	private int sessionDurationMinutes;   // 30 / 45 / 60
	private String frequency;              // DAILY / WEEKLY

	private LocalDate startDate;

	private String therapistId;
	private String therapistNotes;
}
