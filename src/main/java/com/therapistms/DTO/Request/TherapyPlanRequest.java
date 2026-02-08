package com.therapistms.DTO.Request;
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
public class TherapyPlanRequest {
	private String medicalRecordId;
	private TherapyType therapyType;
	private TherapyPlanStatus status;
	private int totalTherapySessions;
	private int sessionDurationMinutes;
	private List<TherapyType> therapies;
	private String frequency;
	private LocalDate startDate;
	private String therapistId;
	private String therapistNotes;
}
