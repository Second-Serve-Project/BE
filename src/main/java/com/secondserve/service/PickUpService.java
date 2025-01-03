package com.secondserve.service;

import com.secondserve.dto.OrderInfoDto;
import com.secondserve.dto.PickUpDto;
import com.secondserve.entity.Cart;
import com.secondserve.entity.Customer;
import com.secondserve.entity.PickUp;
import com.secondserve.jwt.JwtUtil;
import com.secondserve.repository.CartRepository;
import com.secondserve.repository.CustomerRepository;
import com.secondserve.repository.PickUpRepository;
import com.secondserve.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PickUpService {
    private final PickUpRepository pickUpRepository;
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    public void savePickUp(OrderInfoDto orderInfoDto, Cart cart) {
        PickUp pickUp = new PickUp().createPickUp(storeRepository.findByName(orderInfoDto.getStoreName()), cart);
        pickUpRepository.save(pickUp);
    }

    public List<PickUpDto> returnPickUp(String accessToken){
        Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
        List<Cart> carts
        return
    }
}
