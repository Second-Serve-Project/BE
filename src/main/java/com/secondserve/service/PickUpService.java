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
import com.secondserve.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickUpService {
    private final PickUpRepository pickUpRepository;
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    public PickUp savePickUp(OrderInfoDto orderInfoDto, String accessToken) {
        Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
        Cart cart = DtoConverter.convertToDto(orderInfoDto, Cart.class);
        cart.setCartId(2);
        PickUp pickUp = new PickUp().createPickUp(storeRepository.findByName(orderInfoDto.getStoreName()), cart, customer);
        pickUpRepository.save(pickUp);

        return pickUp;
    }

    public List<PickUpDto> returnPickUp(String accessToken){
        Customer customer = customerRepository.findByCustomerId(jwtUtil.getId(accessToken));
        List<PickUpDto> pickUpDtos = new ArrayList<>();
        List<PickUp> pickUps = pickUpRepository.findAllByCustomerId(customer.getCustomerId());
        for(PickUp p : pickUps){
            PickUpDto pickUpDto = PickUpDto.builder()
                    .startTime(p.getStartTime())
                    .endTime(p.getEndTime())
                    .lat(p.getLat())
                    .lon(p.getLon())
                    .address(p.getName())
                    .cart(cartRepository.findAllByCartId(p.getCartId()))
                    .build();
            pickUpDtos.add(pickUpDto);
        }
        return pickUpDtos;
    }
}
