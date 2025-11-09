package com.therapistms.Service;

import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Repository.TherapyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TherapyPlanService {

    @Autowired
    private TherapyPlanRepository therapyPlanRepository;

    // Assign new therapy plan
    public TherapyPlan assignTherapy(TherapyPlan plan) {
        plan.setApproved(true); // default doctor approval
        return therapyPlanRepository.save(plan);
    }

    // Get therapy plans by patient
    public List<TherapyPlan> getPlansByPatient(Long patientId) {
        return therapyPlanRepository.findByPatientId(patientId);
    }

    // Get therapy plans by doctor
    public List<TherapyPlan> getPlansByDoctor(Long doctorId) {
        return therapyPlanRepository.findByDoctorId(doctorId);
    }

    // Get therapy plans by therapist
    public List<TherapyPlan> getPlansByTherapist(Long therapistId) {
        return therapyPlanRepository.findByTherapistId(therapistId);
    }

    // Update therapy plan
    public TherapyPlan updateTherapyPlan(Long id, TherapyPlan updated) {
        TherapyPlan existing = therapyPlanRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTherapyType(updated.getTherapyType());
            existing.setDurationDays(updated.getDurationDays());
            existing.setFrequencyPerDay(updated.getFrequencyPerDay());
            existing.setPrecautions(updated.getPrecautions());
            existing.setApproved(updated.isApproved());
            return therapyPlanRepository.save(existing);
        }
        return null;
    }

    public List<TherapyPlan> getAllTherapies() {
        return therapyPlanRepository.findAll();
    }
}

