package com.therapistms.ENUM;

public enum MedicalRecordStatus {

    // Record has been created in the system,
    // but no medical evaluation has started yet
    CREATED,

    // Patient is under initial medical evaluation,
    // symptoms, history, and preliminary checks are being reviewed
    UNDER_EVALUATION,

    // Doctor has finalized and recorded the diagnosis
    DIAGNOSED,

    // Active medical treatment is ongoing
    // (medication, procedures, or care plan execution)
    IN_TREATMENT,

    // Patient is currently receiving treatment sessions
    // (used when treatment happens over multiple visits)
    HAVING_TREATMENT,

    // Therapy has been advised by the doctor
    // but has not yet started
    THERAPY_RECOMMENDED,
    WAITING_FOR_THERAPIST_APPROVAL,
    THERAPIST_APPROVED,

    // Therapy sessions are actively in progress
    THERAPY_IN_PROGRESS,

    // Diagnostic tests or lab investigations have been prescribed
    TESTS_ORDERED,

    // Tests are completed and the system is waiting
    // for lab results or reports
    AWAITING_RESULTS,

    // Patient requires a follow-up consultation
    // to monitor progress or adjust treatment
    FOLLOW_UP_REQUIRED,

    // Treatment phase is completed and patient
    // is currently in recovery or observation stage
    RECOVERING,

    // Medical case is successfully completed
    // and no further action is required
    COMPLETED,

    // Treatment or evaluation is temporarily paused
    // due to patient, medical, or administrative reasons
    ON_HOLD,

    // Patient has been referred to another doctor,
    // specialist, or healthcare facility
    REFERRED,

    // Case has been escalated due to complexity,
    // lack of improvement, or need for senior expertise
    ESCALATED,

    // Patient is in a critical condition and
    // requires immediate or intensive medical attention
    CRITICAL,

    // Treatment or record has been cancelled
    // before completion
    CANCELLED,

    // Patient has discontinued treatment voluntarily
    // or stopped attending without formal completion
    ABANDONED,

    // Record is closed and archived for legal,
    // compliance, or historical reference
    ARCHIVED
}


