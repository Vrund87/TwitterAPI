package com.v1.TwitterWebAPI.repositories;

import com.v1.TwitterWebAPI.models.TwitterUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<TwitterUser, String> {
    TwitterUser findByname(String username);
}
