package com.therapistms.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapy_specializations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapySpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;   // e.g. "Abhyanga", "Vamana", "Shirodhara"

    @Column(length = 500)
    private String description; // Key details about the therapy

    private String benefits;   // e.g. Detoxification, Relaxation, Improved Digestion

    private String contraindications; // Who should avoid: e.g. pregnancy, old age, weak immunity

    private String duration;   // Typical duration per session (e.g. "45 mins", "3–7 days program")

    private String category;   // Panchakarma, Massage, Oil Therapy, etc.

    private String traditionalReference; // Reference in Ayurvedic texts/scriptures

    private Double costEstimate;  // Approximate cost per session/package

    private boolean active = true; // If specialization is currently offered

    @Builder.Default
    @ManyToMany(mappedBy = "specializations")
    private List<Therapist> therapists = new ArrayList<>();
}