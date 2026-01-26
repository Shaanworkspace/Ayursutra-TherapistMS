package com.therapistms.Service;


import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.RegisterRequestDTO;
import com.therapistms.DTO.Response.*;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.TherapistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapistService {

    private final TherapistRepository therapistRepository;
    private final PatientClient patientClient;

    public Therapist addTherapist(RegisterRequestDTO requestDTO) {
        Therapist therapist = Therapist.builder()
                .therapistName(requestDTO.getName())
                .email(requestDTO.getEmail())
                .userId(requestDTO.getUserId())
                .password(requestDTO.getPassword())
                .build();
        log.info("Saving therapist : {}",therapist);
        return therapistRepository.save(therapist);
    }

    //  Bulk add therapists
    public List<Therapist> createTherapists(List<Therapist> therapists) {
        return therapistRepository.saveAll(therapists);
    }

    //  Get all therapists
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistRepository.findAll().stream()
                .map(this::therapistToDTO)
                .collect(Collectors.toList());
    }

    //  Get therapist by ID
    public TherapistResponseDTO getTherapistById(String id) {
        Therapist therapist = therapistRepository.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("Therapist not found with id " + id));
        return therapistToDTO(therapist);
    }

    public TherapistResponseDTO therapistToDTO(Therapist therapist) {
        log.info("Fetching medical Record from patient");
        List<MedicalRecord> medicalRecordResponseDTOList = patientClient.medicalRecordsByTherapistId(therapist.getUserId());
        log.info("Got Medical Record : {}",medicalRecordResponseDTOList);
        return TherapistResponseDTO.builder()
                .therapistName(therapist.getTherapistName())
                .email(therapist.getEmail())
                .userId(therapist.getUserId())
                .expertise(therapist.getExpertise())
                .qualification(therapist.getQualification())
                .phoneNumber(therapist.getPhoneNumber())
                .medicalRecords(medicalRecordResponseDTOList)
                .yearsOfExperience(therapist.getYearsOfExperience())
                .build();
    }


    //  Login (basic example, don’t use plain-text passwords in real systems!)
    public Therapist login(String email, String password) {
        Therapist therapist = therapistRepository.findByEmail(email);
        if (therapist == null) {
            throw new IllegalArgumentException("No Therapist FOUND with email : " + email);
        }
        if (!therapist.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return therapist;
    }

    //  Delete therapist
    public void deleteTherapist(Long id) {
        if (!therapistRepository.existsById(id)) {
            throw new IllegalArgumentException("Therapist not found with id : " + id);
        }
        therapistRepository.deleteById(id);
    }

}