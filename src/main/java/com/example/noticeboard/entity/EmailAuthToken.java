package com.example.noticeboard.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("emailAuthToken")
@ToString
public class EmailAuthToken {

    @Id
    private String email;

    private String code;

    @TimeToLive
    private Long expiration;
}
