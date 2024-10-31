package com.upcraft.result;

import com.upcraft.dto.ProductDto;
import com.upcraft.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultStatus {

    //게시글, 댓글 작성
    SUCCESS_POST(201, "SUCCESS_POST"),
    SUCCESS_COMMENT(201, "SUCCESS_POST"),
    INVALID_INPUT(400, "INVALID_INPUT"),

    //회원가입
    SIGNUP_MEMBER(201, "SUCCESS_SIGNUP_MEMBER"),
    SIGNUP_SELLER(201, "SUCCESS_SIGNUP_SELLER"),
    ID_ALREADY_EXISTS(409, "ID_ALREADY_EXISTS"),
    NAME_ALREADY_EXISTS(409, "NAME_ALREADY_EXISTS"),
    EMAIL_ALREADY_EXISTS(409, "EMAIL_ALREADY_EXISTS"),

    //로그인
    INVALID_ID(401, "INVALID_ID"),
    INVALID_PW(401, "INVALID_PW"),
    SUCCESS_LOGIN(200, "SUCCESS_LOGIN"),

    //상품 관련 로직
    PROD_REGISTER(201, "SUCCESS_PROD_REGISTER"),
    PROD_MODIFY(200, "SUCCESS_PROD_MODIFY"),
    PROD_LIST(200, "SUCCESS_PROD_LIST"),
    PROD_ALREADY_EXISTS(409, "PRODUCT_ALREADY_EXISTS"),
    PROD_NOT_EXISTS(409, "PRODUCT_NOT_EXISTS"),

    SUCCESS_STORE_SEARCH(200,"SUCCESS_STORE_SEARCH"),
    //비인가, 서버 오류 등
    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    private final int statusCode;
    private final String result;

}