package com.secondserve.service;

import com.secondserve.entity.Appointment;
import com.secondserve.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    @Autowired
    private final AppointmentRepository appointmentRepository;

    public Appointment findById(final Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> {
            return new NotFoundException("appointment 조회 실패");
        });
    }
}
