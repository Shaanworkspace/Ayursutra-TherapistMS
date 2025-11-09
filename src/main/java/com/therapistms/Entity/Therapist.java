package com.therapistms.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Therapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)  // Required & max size
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)  // Avoid duplicate emails
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)   // Mobile number
    private String phoneNumber;

    @Column(length = 150)
    private String qualification;   // e.g., BAMS, MD Ayurveda

    private Integer yearsOfExperience;

    @Column(length = 150)
    private String expertise;       // e.g., Panchakarma, Marma Therapy

    @Column(length = 100)
    private String languagesSpoken; // e.g., Hindi, English, Sanskrit

    @Column(length = 255)
    private String clinicLocation;  // place of practice

    @Lob
    private String bio;             // full description of therapist


    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<TherapyPlan> therapyPlans = new ArrayList<>();

    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL)
    private List<ScheduleSlot> scheduleSlots = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "therapist_have_specialization",
            joinColumns = @JoinColumn(name = "therapist_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private List<TherapySpecialization> specializations = new ArrayList<>();


    private List<Integer> medicalRecordIds;

    private LocalDate registrationDate;
    @PrePersist
    protected void onRegister() {
        this.registrationDate = LocalDate.now();
    }
}