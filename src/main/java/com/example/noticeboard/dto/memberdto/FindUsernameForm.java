package com.example.noticeboard.dto.memberdto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class FindUsernameForm {

    @NotBlank
    @Email
    private String email;

    @Size(min = 6, max = 6)
    private String code;
}
