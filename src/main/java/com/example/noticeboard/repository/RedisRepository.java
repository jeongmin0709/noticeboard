package com.example.noticeboard.repository;


import com.example.noticeboard.entity.EmailAuthToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RedisRepository extends CrudRepository<EmailAuthToken, String> {

}
