package com.example.noticeboard.exception.exception_handler;

import com.example.noticeboard.exception.custom_exception.CustomException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.ExceptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class RestControllerExceptionHandler {

    private final MessageSource messageSource;

    //커스텀 예외 핸들러
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> CustomExHandler(CustomException e){
        ExceptionDTO exceptionDTO = makeExceptionDTO(e.getErrorCode());
        return new ResponseEntity<>(exceptionDTO, e.getErrorCode().getStatus());
    }

    //입력값 유효성 검증 예외
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> NotValidEx(BindException e){
        ExceptionDTO exceptionDTO = makeExceptionDTO(e, ErrorCode.VALIDATION_ERROR);
        return new ResponseEntity<>(exceptionDTO, ErrorCode.VALIDATION_ERROR.getStatus());
    }

    // 이미지 업로드 예외
    @ExceptionHandler
    public ResponseEntity<ExceptionDTO> imageExHandler(NoSuchFileException e){
        ExceptionDTO exceptionDTO = makeExceptionDTO(ErrorCode.NOT_FOUND_FILE);
        return new ResponseEntity<>(exceptionDTO, ErrorCode.NOT_FOUND_FILE.getStatus());
    }

    // 이메일 예외
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDTO> emailSendExHandler(MessagingException e){
        ExceptionDTO exceptionDTO = makeExceptionDTO(ErrorCode.MESSAGING_ERROR);
        return new ResponseEntity<>(exceptionDTO, ErrorCode.MESSAGING_ERROR.getStatus());
    }


    // ExceptionDTO 생성 함수
    private ExceptionDTO makeExceptionDTO(ErrorCode errorCode) {
        return ExceptionDTO.builder()
                .code(errorCode.name())
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    // ExceptionDTO 생성 함수
    private ExceptionDTO makeExceptionDTO(BindException exception, ErrorCode errorCode) {
        List<ExceptionDTO.ValidationError> fieldErrorList = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ExceptionDTO.ValidationError.of(fieldError.getField(), getErrorMessage(fieldError)))
                .collect(Collectors.toList());

        return ExceptionDTO.builder()
                .code(errorCode.name())
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .fieldErrors(fieldErrorList)
                .build();
    }
    // errors.properties 에 있는 필드 에러 메시지를 가져오는 함수
    private String getErrorMessage(FieldError error) {
        String[] codes = error.getCodes();
        for (String code : codes) {
            try {
                return messageSource.getMessage(code, error.getArguments(), Locale.KOREA);
            } catch (NoSuchMessageException ignored) {}
        }
        return error.getDefaultMessage();
    }
}
