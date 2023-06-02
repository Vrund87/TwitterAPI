package com.v1.TwitterWebAPI.models;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "tweets_index")
public class ElasticsearchTwitterUser {
    @Field(type = FieldType.Long)
    private long tweet_id;

    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Date)
    private String created_time;

    @Field(type = FieldType.Text)
    private List<String> hashtag;

    @Field(type = FieldType.Keyword)
    private String username;

    public long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(long tweet_id) {
        this.tweet_id = tweet_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public List<String> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
