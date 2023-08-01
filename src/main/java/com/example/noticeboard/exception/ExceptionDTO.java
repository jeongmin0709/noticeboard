package com.example.noticeboard.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
public class ExceptionDTO {

    private final String code;

    private final String message;

    private final int status;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> fieldErrors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {

        private final String field;
        private final String message;

        public static ValidationError of(String field, String message) {

            return ValidationError.builder()
                    .field(field)
                    .message(message)
                    .build();
        }
    }

}
