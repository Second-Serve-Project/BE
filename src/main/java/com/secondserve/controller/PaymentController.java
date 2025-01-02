package com.secondserve.controller;

import com.secondserve.docs.PaymentDocs;
import com.secondserve.dto.OrderInfoDto;
import com.secondserve.entity.Customer;
import com.secondserve.jwt.JwtUtil;
import com.secondserve.repository.CustomerRepository;
import com.secondserve.service.PaymentService;
import com.secondserve.util.PayUtil;
import com.siot.IamportRestClient.IamportClient;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pay")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentDocs {
    private IamportClient iamportClient;
    private Payment payment;
    private final PayUtil payUtil;
    private final PaymentService paymentService;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init(){
        log.info("PayUtil instance: {}", payUtil);
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }
    @PostMapping("/api/order/payment/complete")
    public ResponseEntity<String> paymentComplete(@RequestHeader("Authorization") String accessToken, @RequestBody OrderInfoDto orderInfo) throws IOException {

        String token = paymentService.getToken();
        int amount = paymentService.paymentInfo(orderInfo.getImpUid(), token);
        try {
            // 주문 시 사용한 포인트
           int usedPoint = orderInfo.getUsedPoint();
           Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
           System.out.println(customer.getName());
           paymentService.paymentSave(orderInfo, customer);
           if (customer != null) {
                Long point = customer.getPoint();
                // 사용된 포인트가 유저의 포인트보다 많을 때
                if (point < usedPoint) {
                   paymentService.paymentCancel(token, orderInfo.getImpUid(), amount, "유저 포인트 오류");
                   return new ResponseEntity<String>("유저 포인트 오류", HttpStatus.BAD_REQUEST);
               }

            } else {
                // 로그인 하지않았는데 포인트사용 되었을 때
                if (usedPoint != 0) {
                    paymentService.paymentCancel(token, orderInfo.getImpUid(), amount, "비회원 포인트사용 오류");
                    return new ResponseEntity<String>("비회원 포인트 사용 오류", HttpStatus.BAD_REQUEST);
                }
            }

            // 계산 된 금액과 실제 금액이 다를 때
            if (amount != orderInfo.getAmount()) {
                paymentService.paymentCancel(token, orderInfo.getImpUid(), amount, "결제 금액 오류");
                return new ResponseEntity<String>("결제 금액 오류, 결제 취소", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("주문이 완료되었습니다", HttpStatus.OK);

        } catch (Exception e) {
            paymentService.paymentCancel(token, orderInfo.getImpUid(), amount, "결제 에러");
            return new ResponseEntity<String>("결제 에러", HttpStatus.BAD_REQUEST);
        }


    }
}
