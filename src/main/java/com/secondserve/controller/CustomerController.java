package com.secondserve.controller;

import com.secondserve.docs.CustomerDocs;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.CustomerDto;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cus")
public class CustomerController implements CustomerDocs {
    @Autowired
    private CustomerServiceImpl customerService;
    @GetMapping("/info")
    @Override
    public ApiResponse<CustomerDto.Page> getCustomerInfo(@RequestHeader("Authorization") String token){
        return ApiResponse.fromResultStatus(ResultStatus.STORE_SPEC,customerService.fetchCustomerInfo(token));
    }
}
