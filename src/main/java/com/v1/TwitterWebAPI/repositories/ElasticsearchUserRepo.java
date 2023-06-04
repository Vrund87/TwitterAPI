package com.v1.TwitterWebAPI.repositories;

import com.v1.TwitterWebAPI.models.ElasticsearchTwitterUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ElasticsearchUserRepo extends ElasticsearchRepository<ElasticsearchTwitterUser, String> {
}
