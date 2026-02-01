package com.therapistms.DTO.Response;

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
public class TherapyPlanDTO {

	private String therapyPlanId;

	private TherapyType therapyType;

	private int totalSessions;
	private int completedSessions;

	private int sessionDurationMinutes;
	private String frequency;
	private TherapistDecisionStatus therapistDecisionStatus;
	private TherapyPlanStatus status;

	private LocalDate startDate;


	private String therapistId;
	private String therapistName;

	private String therapistNotes;
}
