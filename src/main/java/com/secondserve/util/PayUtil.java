package com.secondserve.util;

import com.secondserve.entity.Appointment;
import com.secondserve.enumeration.PayStatus;
import com.secondserve.service.AppointmentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class PayUtil {

    private final AppointmentService appointmentService;
    public HttpHeaders makeHttpHeader() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        responseHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return responseHeaders;
    }
    public Payment extractPayment(final String imp_uid, final IamportClient iamportClient)  {
        return iamportClient.paymentByImpUid(imp_uid).getResponse();
    }

    public void validateAppointmentPayStatus(final Long appointmentId) throws IllegalAccessException{
        final Appointment appointment = appointmentService.findById(appointmentId);

        if (appointment.getPayStatus() == PayStatus.COMPLETED) {
            throw new IllegalAccessException();
        }
    }
}
