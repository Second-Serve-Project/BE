package com.secondserve.controller;


import com.secondserve.docs.RegisterDocs;
import com.secondserve.dto.CustomerDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.result.ResultStatus;
import com.secondserve.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController implements RegisterDocs {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RegisterService registerService;

    @GetMapping("/checkid")
    public Boolean checkIdAvailability(@RequestParam String id, @RequestParam String role){
        return registerService.fetchExistById(id/*, role*/);
    }
    /*@GetMapping("/checkemail")
    public Boolean checkEmailAvailability(@RequestParam String email, @RequestParam String role){
        return registerService.fetchExistByEmail(email, role);
    }*/

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "유저 정보를 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    public ApiResponse<Void> signUp(@Valid @RequestBody CustomerDto.Register registerRequest) {
        return registerService.trySignUp(registerRequest);
    }



}
