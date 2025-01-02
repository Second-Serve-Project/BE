package com.secondserve.service;


import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.CustomerDto;
import com.secondserve.entity.Customer;
import com.secondserve.event.UserEvent;
import com.secondserve.repository.CustomerRepository;
import com.secondserve.enumeration.ResultStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@AllArgsConstructor
public class RegisterService {
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean fetchExistById(String id) {
        return customerRepository.existsByCustomerId(id);
    }


    @Transactional
    public ApiResponse<Void> trySignUp(@Valid CustomerDto.Register registerRequest) {
        String customerId = registerRequest.getId();
        String encodedPW = passwordEncoder.encode(registerRequest.getPassword());

        // 중복된 ID가 있는지 확인
        if (!fetchExistById(customerId)) {
            // 고객 정보 생성
            Customer customer = Customer.builder()
                    .customerId(customerId)
                    .password(encodedPW)
                    .build();

            // 고객 정보 저장
            customerRepository.save(customer);

            // 회원가입 이벤트 발행
            UserEvent.Register event = new UserEvent.Register(registerRequest.getId(), registerRequest.getPassword());
            eventPublisher.publishEvent(event);

            System.out.println(event);

            // 성공적인 회원가입 응답 반환
            return ApiResponse.fromResultStatus(ResultStatus.SIGNUP_MEMBER);
        }

        // 실패 시 내부 서버 오류 반환
        return ApiResponse.fromResultStatus(ResultStatus.INTERNAL_SERVER_ERROR);
    }

}
