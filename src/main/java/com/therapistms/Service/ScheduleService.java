package com.therapistms.Service;
import com.therapistms.DTO.Response.ScheduleSlotDTO;
import com.therapistms.DTO.Response.WeeklyScheduleDTO;
import com.therapistms.ENUM.SlotStatus;
import com.therapistms.Entity.ScheduleSlot;
import com.therapistms.Entity.Therapist;
import com.therapistms.Repository.ScheduleSlotRepository;
import com.therapistms.Repository.TherapistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleSlotRepository slotRepository;
    private final TherapistRepository therapistRepository;
    public List<WeeklyScheduleDTO> generateWeeklySchedule(Long therapistId,
                                                          LocalDate startOfWeek,
                                                          List<LocalTime[]> slotTimes) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new IllegalArgumentException("Therapist not found with id: " + therapistId));

        List<WeeklyScheduleDTO> weeklySchedule = new ArrayList<>();

        for (int i = 0; i < 7; i++) {  // generate 7 days
            LocalDate currentDay = startOfWeek.plusDays(i);

            List<ScheduleSlotDTO> slotsForDay = new ArrayList<>();
            for (LocalTime[] times : slotTimes) {
                ScheduleSlot slot = ScheduleSlot.builder()
                        .date(currentDay)
                        .startTime(times[0])
                        .endTime(times[1])
                        .status(SlotStatus.AVAILABLE)
                        .therapist(therapist)
                        .build();

                ScheduleSlot saved = slotRepository.save(slot);

                slotsForDay.add( new ScheduleSlotDTO(
                        saved.getId(),
                        saved.getDate(),
                        saved.getStartTime(),
                        saved.getEndTime(),
                        saved.getStatus(),
                        null
                ));
            }
            weeklySchedule.add(new WeeklyScheduleDTO(
                    currentDay.getDayOfWeek().name(),
                    slotsForDay
            ));
        }
        return weeklySchedule;
    }
    @Transactional
    public ScheduleSlot bookSlot(Long therapistId, Long slotId, Long patientId) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Therapist not found with id " + therapistId));

        ScheduleSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Slot not found with id " + slotId));

        if (!slot.getTherapist().getId().equals(therapistId)) {
            throw new IllegalArgumentException("❌ Slot does not belong to this therapist");
        }

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalArgumentException("❌ Slot already booked or unavailable");
        }



        slot.setBookedByPatientId(null);
        slot.setStatus(SlotStatus.BOOKED);

        return slotRepository.save(slot);
    }
}