package com.v1.TwitterWebAPI.repositories;

import com.v1.TwitterWebAPI.models.TwitterUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<TwitterUser, String> {
    TwitterUser findByname(String username);
}
