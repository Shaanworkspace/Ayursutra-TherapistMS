package com.therapistms.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TherapyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String therapyType;
    private int durationDays;
    private int frequencyPerDay;
    private String precautions;
    private boolean approved;

    private Integer patientId;


    private Integer doctorId;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

}
