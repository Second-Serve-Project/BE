package com.secondserve.service;

import com.secondserve.dto.ApiResponse;
import com.secondserve.entity.Customer;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.jwt.JwtUtil;
import com.secondserve.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private final CustomerRepository customerRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public ApiResponse<Void> updatePassword(String accessToken, String newPassword){
        Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        return ApiResponse.fromResultStatus(ResultStatus.SUCCESS_PW_CHANGE);
    }
}
