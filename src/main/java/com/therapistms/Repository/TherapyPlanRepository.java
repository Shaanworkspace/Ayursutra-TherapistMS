package com.therapistms.Repository;



import com.therapistms.Entity.TherapyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TherapyPlanRepository extends JpaRepository<TherapyPlan, Long> {
    // Get all therapy plans by patient
    List<TherapyPlan> findByPatientId(Long patientId);

    // Get all therapy plans assigned by a doctor
    List<TherapyPlan> findByDoctorId(Long doctorId);

    // Get therapy plans handled by therapist
    List<TherapyPlan> findByTherapistId(Long therapistId);
}