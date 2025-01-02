package com.secondserve.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.secondserve.dto.ApiResponse;
import com.secondserve.dto.CartDto;
import com.secondserve.dto.OrderInfoDto;
import com.secondserve.entity.Appointment;
import com.secondserve.entity.Cart;
import com.secondserve.entity.Customer;
import com.secondserve.entity.PaymentEntity;
import com.secondserve.enumeration.ResultStatus;
import com.secondserve.repository.CartRepository;
import com.secondserve.repository.PaymentRepository;
import com.secondserve.util.DtoConverter;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${imp.api.key}")
    private String impKey;

    @Value("${imp.api.secretkey}")
    private String impSecret;

    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    @Data
    private class Response{
        private PaymentInfo response;
    }

    @Data
    private class PaymentInfo{
        private int amount;
    }



    public String getToken() throws IOException {

        HttpsURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/users/getToken");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        JsonObject json = new JsonObject();

        json.addProperty("imp_key", impKey);
        json.addProperty("imp_secret", impSecret);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        Gson gson = new Gson();

        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        System.out.println("response: " + response);

        String token = gson.fromJson(response, Map.class).get("access_token").toString();

        br.close();
        conn.disconnect();

        return token;
    }

    public int paymentInfo(String imp_uid, String access_token) throws IOException {
        HttpsURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/payments/" + imp_uid);

        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", access_token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            Gson gson = new Gson();
            Response response = gson.fromJson(br.readLine(), Response.class);
            br.close();
            conn.disconnect();
            return response.getResponse().getAmount();
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                errorResponse.append(line);
            }
            System.out.println("Error Response: " + errorResponse.toString());
            br.close();
            conn.disconnect();
            return -1; // 오류 처리
        }
    }

    public void paymentSave(OrderInfoDto orderInfoDto, Customer customer) {
        // Cart 저장 및 cartId 반환
        System.out.println("paymentSave is called." + customer.getName());
        long cartId = cartSave(orderInfoDto.getCart());
        // PaymentEntity 생성 및 저장
        PaymentEntity payment = PaymentEntity.createPaymentEntity(
                orderInfoDto.getImpUid(),
                cartId, // cartId 저장
                orderInfoDto.getUsedPoint(),
                customer
        );

        paymentRepository.save(payment);
    }


    public long findNextCartId() {
        return cartRepository.findMaxCartId().orElse(0L) + 1;
    }
    public long cartSave(List<CartDto> cartDtoList){
        long cartId = findNextCartId();
        System.out.println("cartSave is called. " + cartId);
        for (CartDto o : cartDtoList){
            Cart cart = DtoConverter.convertToDto(o, Cart.class);
            cart.setCartId(cartId);
            cartRepository.save(cart);
        }
        return cartId;
    }
    public void paymentCancel(String access_token, String imp_uid, int amount, String reason) throws IOException  {

        HttpsURLConnection conn = null;
        URL url = new URL("https://api.iamport.kr/payments/cancel");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", access_token);

        conn.setDoOutput(true);

        JsonObject json = new JsonObject();

        json.addProperty("reason", reason);
        json.addProperty("imp_uid", imp_uid);
        json.addProperty("amount", amount);
        json.addProperty("checksum", amount);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        br.close();
        conn.disconnect();
        PaymentEntity payment = paymentRepository.findByImpUid(imp_uid);
        payment.refund();
        paymentRepository.save(payment);

    }





}