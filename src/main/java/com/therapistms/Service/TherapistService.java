package com.therapistms.Service;


import com.therapistms.Client.PatientClient;
import com.therapistms.DTO.Request.RegisterRequestDTO;
import com.therapistms.DTO.Response.*;
import com.therapistms.Entity.Therapist;
import com.therapistms.Entity.TherapyPlan;
import com.therapistms.Repository.TherapistRepository;
import com.therapistms.Repository.TherapyPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapistService {

    private final TherapistRepository therapistRepository;
    private final PatientClient patientClient;
    private final TherapyPlanRepository therapyPlanRepository;

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

    public List<String> medicalRecordIdsByTherapistId(String therapistId){
        List<TherapyPlan> therapyPlanList = therapyPlanRepository.findAllByTherapistId(therapistId);
        List<String> medicalRecordIds = new ArrayList<>();
        for(TherapyPlan t : therapyPlanList){
            medicalRecordIds.add(t.getMedicalRecordId());
        }
        return medicalRecordIds;
    }

    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByMedicalRecordIds(List<String> medicalRecordIds){
        log.info("Fetching medical Record from patient");
       List<MedicalRecordResponseDTO> medicalRecordResponseDTOList = patientClient.getMedicalRecordsByIds(medicalRecordIds);
        log.info("Got Medical Record : {}",medicalRecordResponseDTOList);
        if(medicalRecordResponseDTOList.isEmpty()){
            log.info("NO record found by medical List Numbers .{}",medicalRecordIds);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(medicalRecordResponseDTOList);
    }

    public TherapistResponseDTO therapistToDTO(Therapist therapist) {
        List<String> medicalRecordIds = medicalRecordIdsByTherapistId(therapist.getUserId());

        List<MedicalRecordResponseDTO> medicalRecordResponseDTOList = new ArrayList<>();

        if(!medicalRecordIds.isEmpty())
            medicalRecordResponseDTOList = getMedicalRecordsByMedicalRecordIds(medicalRecordIds).getBody();

        if(medicalRecordResponseDTOList!=null) log.info("Fetching Complete of Medical Record from patient");
        else log.warn("No medical Record found with this Therapist id : {}",therapist.getUserId());

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


    public void deleteTherapist(Long id) {
        if (!therapistRepository.existsById(id)) {
            throw new IllegalArgumentException("Therapist not found with id : " + id);
        }
        therapistRepository.deleteById(id);
    }

    public TherapistProfileDTO therapistToProfileDTO(Therapist therapist) {
        return TherapistProfileDTO.builder()
                .therapistName(therapist.getTherapistName())
                .email(therapist.getEmail())
                .userId(therapist.getUserId())
                .expertise(therapist.getExpertise())
                .qualification(therapist.getQualification())
                .phoneNumber(therapist.getPhoneNumber())
                .yearsOfExperience(therapist.getYearsOfExperience())
                .build();
    }
}