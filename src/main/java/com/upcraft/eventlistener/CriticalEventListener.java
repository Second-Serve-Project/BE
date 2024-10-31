package com.upcraft.eventlistener;


import com.upcraft.annotation.CriticalEvent;
import com.upcraft.event.UserEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CriticalEventListener {

    @EventListener
    public void handleCriticalEvent(UserEvent.Register event) {
        System.out.println(event.toString());
        System.out.println(event.getClass());
        // 이벤트 클래스에 @CriticalEvent 어노테이션이 있는지 확인
        if (event.getClass().isAnnotationPresent(CriticalEvent.class)) {
            CriticalEvent critical = event.getClass().getAnnotation(CriticalEvent.class);
            String importance = critical.value();

            // 중요도에 따른 처리 로직
            if ("HIGH".equals(importance)) {
                System.out.println("Handling high importance event: " + event.toString());
                // 중요 이벤트에 대한 특별 처리
            } else {
                System.out.println("Handling regular event: " + event);
            }
        } else {
            System.out.println("No critical event annotation found.");
        }
    }
}
