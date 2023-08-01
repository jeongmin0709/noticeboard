package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthCodeResultDTO {
    Boolean isAuthentication;
    String code;
    String message;
}
