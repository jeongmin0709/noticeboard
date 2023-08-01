package com.example.noticeboard.eannotation;

import com.example.noticeboard.sercurity.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser{
    String username() default "mockMember";

    String[] roles() default { "ROLE_USER" };

    String password() default "password";
}
