package com.secondserve.util;

import org.springframework.beans.BeanUtils;

public class    DtoConverter {
    public static <D, E> D convertToDto(E entity, Class<D> dtoClass) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error while converting to DTO", e);
        }
    }
}
