package com.upcraft.controller;


import com.upcraft.docs.RegisterDocs;
import com.upcraft.dto.CustomerDto;
import com.upcraft.dto.ResponseDto;
import com.upcraft.result.ResultStatus;
import com.upcraft.service.RegisterService;
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
    public ResponseDto<Void> signUp(@Valid @RequestBody CustomerDto.Register registerRequest){

        ResultStatus signUpRes = registerService.trySignUp(registerRequest);

        // 회원가입 성공 시 이벤트 발행
        if (ResultStatus.SIGNUP_MEMBER.getResult().equals(signUpRes.getResult())||
                ResultStatus.SIGNUP_SELLER.getResult().equals(signUpRes.getResult())) {
            return ResponseDto.fromResultStatus(signUpRes);
        }
        return ResponseDto.fromResultStatus(ResultStatus.BAD_REQUEST);
    }

}
