package com.example.noticeboard.dto.memberdto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ModifyEmailForm {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_\\-]{5,20}$")
    private String username;

    @NotBlank
    @Email
    private String newEmail;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

}
