package com.secondserve.event;


import com.secondserve.annotation.CriticalEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
public class UserEvent {
    @Getter
    @CriticalEvent("HIGH")
    @AllArgsConstructor
    public static class Register{
        private final String userId;
        private final String email;
    }
    @Getter
    @CriticalEvent("HIGH")
    @AllArgsConstructor
    public static class Buy{
        private final String userId;
        private final String productName;
        private final String shipment;
    }
}
