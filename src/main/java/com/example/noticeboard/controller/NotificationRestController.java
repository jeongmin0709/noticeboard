package com.example.noticeboard.controller;

import com.example.noticeboard.dto.NotificationDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.entity.notification.Notification;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/notification")
public class NotificationRestController {

    private final NotificationService notificationService;
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @PreAuthorize("isAuthenticated()")
    public SseEmitter subscribe(@AuthenticationPrincipal MemberDTO memberDTO){
        return notificationService.subscribe(memberDTO.getUsername());
    }

    @PatchMapping("/read")
    @PreAuthorize("isAuthenticated()")
    public void isReadUpdate(@RequestBody List<Long> idList){
        notificationService.updateNotificationIsRead(idList);
    }
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public SliceResultDTO<NotificationDTO, Notification> getList(SliceRequestDTO sliceRequestDTO, @AuthenticationPrincipal MemberDTO memberDTO){
        return notificationService.getNotificationList(sliceRequestDTO, memberDTO.getUsername());
    }
}
