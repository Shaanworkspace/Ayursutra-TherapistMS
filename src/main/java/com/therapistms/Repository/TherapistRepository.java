package com.therapistms.Repository;


import com.therapistms.Entity.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
    List<Therapist> findByExpertise(String expertise);

    Therapist findByEmail(String email);
}