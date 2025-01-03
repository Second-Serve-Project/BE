package com.secondserve.controller;

import com.secondserve.docs.PickUpDocs;
import com.secondserve.dto.OrderInfoDto;
import com.secondserve.dto.PickUpDto;
import com.secondserve.entity.Cart;
import com.secondserve.entity.PickUp;
import com.secondserve.service.PickUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickup")
@RequiredArgsConstructor
public class PickUpController implements PickUpDocs {
    @Autowired
    private final PickUpService pickUpService;

    @GetMapping("/get")
    public ResponseEntity<List<PickUpDto>> getPickUpList(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(pickUpService.returnPickUp(accessToken));
    }
    @PostMapping("/post")
    public ResponseEntity<PickUp> savePickUp(@RequestHeader("Authorization") String accessToken, @RequestBody OrderInfoDto orderInfoDto) {
        return ResponseEntity.ok(pickUpService.savePickUp(orderInfoDto, accessToken));
    }
}