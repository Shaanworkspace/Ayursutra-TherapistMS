package com.therapistms.ENUM;

public enum TherapyPlanStatus {
	NOT_ASSIGNED,   // therapy recommended but plan not created yet
	ASSIGNED,       // plan created & therapist assigned, not started
	ACTIVE,         // sessions are ongoing
	ON_HOLD,        // temporarily paused (medical / personal / admin reason)
	CANCELLED,      // stopped before completion
	COMPLETED       // all sessions completed
}
