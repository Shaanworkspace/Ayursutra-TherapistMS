package com.therapistms.Repository;


import com.therapistms.Entity.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
    List<Therapist> findByExpertise(String expertise);

    Therapist findByEmail(String email);

    Optional<Therapist> findByUserId(String userId);

    boolean existsByEmail(String email);

	Therapist findTherapistByUserId(String userId);

	Therapist findTherapistByEmail(String email);

	boolean existsTherapistByUserId(String userId);
}