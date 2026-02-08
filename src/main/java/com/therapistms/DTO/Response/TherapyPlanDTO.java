package com.therapistms.DTO.Response;

import com.therapistms.ENUM.TherapyPlanStatus;
import com.therapistms.ENUM.TherapyType;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyPlanDTO {

	private String therapyPlanId;
	private int totalTherapySessions;
	private int bookedTherapySessions;
	private int completedTherapySessions;

	private int sessionDurationMinutes;
	private String frequency;
	private TherapyPlanStatus status;
	private List<TherapyType> therapies;

	private LocalDate startDate;
	private String therapistId;
	private String therapistName;

	private String therapistNotes;
}
