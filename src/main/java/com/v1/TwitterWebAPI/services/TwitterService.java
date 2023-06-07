package com.v1.TwitterWebAPI.services;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
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

import java.util.*;

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

    public List<String> searchTweetsByKeyword(String query){
        Query queryforhashtag = MatchQuery.of(m -> m
                .field("hashtag")
                .query(query))._toQuery();

        Query queryformessage = MatchQuery.of(m -> m
                .field("message")
                .query(query))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .should(queryforhashtag)
                .should(queryformessage))._toQuery();

        NativeQuery searchQuery2 = NativeQuery.builder()
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes().build())
                .withQuery(boolQuery)
                .withSort(Sort.by(Sort.Direction.ASC, "created_time"))
                .build();

        List<String> tweets = new ArrayList<>();
        SearchHits<ElasticsearchTwitterUser> searchHits = elasticsearchOperations.search(searchQuery2, ElasticsearchTwitterUser.class);
        System.out.println(searchHits);
        searchHits.forEach(searchHit ->  tweets.add(searchHit.getContent().getMessage()));
        return tweets;
    }

    public List<String> searchTweetsByTimeRange(String sdate, String edate, String keyword){
        Query matchPhraseQuery = MatchPhraseQuery.of(m -> m
                .field("message")
                .query(keyword))._toQuery();
        Query rangeQuery = RangeQuery.of(r -> r
                .field("created_time")
                .gte(JsonData.fromJson(sdate))
                .lte(JsonData.fromJson(edate)))._toQuery();

        Query boolQuery = BoolQuery.of(b -> b
                .must(matchPhraseQuery)
                .filter(rangeQuery))._toQuery();

        NativeQuery searchQuery2 = NativeQuery.builder()
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes().build())
                .withQuery(boolQuery)
                .withSort(Sort.by(Sort.Direction.ASC, "created_time"))
                .build();

        List<String> tweets = new ArrayList<>();
        SearchHits<ElasticsearchTwitterUser> searchHits = elasticsearchOperations.search(searchQuery2, ElasticsearchTwitterUser.class);
        searchHits.forEach(searchHit ->  tweets.add(searchHit.getContent().getMessage()));
        return tweets;
    }

    public List<String> searchTweetsByUsername(String username){
        Query matchquery = MatchQuery.of(m -> m
                .field("username")
                .query(username))._toQuery();

        NativeQuery searchQuery2 = NativeQuery.builder()
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes().build())
                .withQuery(matchquery)
                .withSort(Sort.by(Sort.Direction.ASC, "created_time"))
                .build();

        List<String> tweets = new ArrayList<>();
        SearchHits<ElasticsearchTwitterUser> searchHits = elasticsearchOperations.search(searchQuery2, ElasticsearchTwitterUser.class);
        searchHits.forEach(searchHit ->  tweets.add(searchHit.getContent().getMessage()));
        return tweets;
    }
}
