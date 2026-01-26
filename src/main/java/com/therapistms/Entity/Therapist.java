package com.therapistms.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapist")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Therapist {
    @Id
    private String userId;
    private String therapistName;

    private String email;
    private String password;


    private String phoneNumber;
    private String qualification;   // e.g., BAMS, MD Ayurveda
    private Integer yearsOfExperience;
    private String expertise;
    private String bio;             // full description of therapist
}