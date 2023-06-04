package com.v1.TwitterWebAPI.services;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.v1.TwitterWebAPI.dataCache.CacheTwitter;
import com.v1.TwitterWebAPI.models.ElasticsearchTwitterUser;
import com.v1.TwitterWebAPI.models.TwitterUser;
import com.v1.TwitterWebAPI.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TwitterService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CacheTwitter<TwitterUser> twitterUserCache;

    @Autowired
    public ElasticsearchOperations elasticsearchOperations;

    public List<TwitterUser> getAllUsers(){
        return userRepo.findAll();
    }

    public TwitterUser getUser(String username){
        return userRepo.findByname(username);
    }

    public List<String> getCommonFollowers(String username1, String username2){
        TwitterUser user1 = twitterUserCache.get(username1);
        TwitterUser user2 = twitterUserCache.get(username2);
        if(user1 == null){
            user1 = this.getUser(username1);
            twitterUserCache.add(username1, user1);
        }
        if(user2 == null){
            user2 = this.getUser(username2);
            twitterUserCache.add(username2, user2);
        }

        List<String> commonFollowers = new ArrayList<String>();
        if(user1 == null || user2 == null) return commonFollowers;

        List<String>followers2 = user2.getfollowers();
        Set<String> potential = new HashSet<String>(user1.getfollowers());

        for(String follower : followers2){
            if(potential.contains(follower)){
                commonFollowers.add(follower);
            }
        }
        return commonFollowers;
    }

    public void storeUser(TwitterUser twitterUser){
        userRepo.save(twitterUser);
    }

    public List<String> searchTweets(String query){
        Query multimatchquery = MatchQuery.of(m -> m
                .field("message")
                .field("hashtag")
                .query(query))._toQuery() ;

        NativeQuery searchQuery2 = NativeQuery.builder()
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes().build())
                .withQuery(multimatchquery)
                .withSort(Sort.by(Sort.Direction.ASC, "created_time"))
                .build();

        List<String> tweets = new ArrayList<>();
        SearchHits<ElasticsearchTwitterUser> searchHits = elasticsearchOperations.search(searchQuery2, ElasticsearchTwitterUser.class);
        searchHits.forEach(searchHit ->  tweets.add(searchHit.getContent().getMessage()));
        return tweets;
    }
}
