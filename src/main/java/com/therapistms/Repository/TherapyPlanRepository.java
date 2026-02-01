package com.therapistms.Repository;

import com.therapistms.Entity.TherapyPlan;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface TherapyPlanRepository extends JpaRepository<TherapyPlan, String> {

	List<TherapyPlan> findAllByTherapistId(String therapistId);

	boolean existsByMedicalRecordIdAndTherapistId(String medicalRecordId, String therapistId);

	TherapyPlan findTherapyPlansByMedicalRecordId(String medicalRecordId);

	Optional<TherapyPlan> findTherapyPlanByTherapyPlanId(String therapyPlanId);

	TherapyPlan findTherapyPlansByTherapyPlanId(String therapyPlanId);
}