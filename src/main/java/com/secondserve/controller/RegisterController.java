package com.secondserve.controller;


import com.secondserve.docs.RegisterDocs;
import com.secondserve.dto.CustomerDto;
import com.secondserve.dto.ApiResponse;
import com.secondserve.result.ResultStatus;
import com.secondserve.service.RegisterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/r")
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
    public ApiResponse<Void> signUp(@Valid @RequestBody CustomerDto.Register registerRequest) {
        return registerService.trySignUp(registerRequest);
    }


}
