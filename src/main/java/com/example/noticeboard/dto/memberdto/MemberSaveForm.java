package com.example.noticeboard.dto.memberdto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class MemberSaveForm {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_\\-]{5,20}$")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    private String password;

    @NotBlank
    private String passwordConfirm;

    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,6}$")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

}
