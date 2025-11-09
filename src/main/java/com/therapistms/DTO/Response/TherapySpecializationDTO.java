package com.therapistms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapySpecializationDTO {
    private Long id;
    private String name;
    private String description;
    private String benefits;
    private String contraindications;
    private String duration;
    private String category;
    private String traditionalReference;
    private Double costEstimate;
    private boolean active;

    // only id + name of therapists (lightweight)
    private List<TherapistResponseDTO> therapists;
}