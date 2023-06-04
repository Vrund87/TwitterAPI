package com.v1.TwitterWebAPI.controllers;

import com.v1.TwitterWebAPI.dataCache.CacheTwitter;
import com.v1.TwitterWebAPI.models.ElasticsearchTwitterUser;
import com.v1.TwitterWebAPI.models.TwitterUser;
import com.v1.TwitterWebAPI.services.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TwitterController {
    @Autowired
    private TwitterService twitterService;

    @Autowired
    private CacheTwitter<TwitterUser> twitterUserCache;

    @GetMapping("/")
    public List<TwitterUser> getAllUsers(){
        return twitterService.getAllUsers();
    }

    @PostMapping("/users/store")
    public List<TwitterUser> storeUser(@RequestBody TwitterUser twitterUser){
        twitterService.storeUser(twitterUser);
        twitterUserCache.add(twitterUser.getName(), twitterUser);
        return twitterService.getAllUsers();
    }

    @GetMapping("/users/{username}")
    public TwitterUser getUser(@PathVariable String username){
        TwitterUser cachedUserRecord = twitterUserCache.get(username);
        if(cachedUserRecord != null) {
            System.out.println("User record found in cache : " + cachedUserRecord.getName());
            return cachedUserRecord;
        }

        TwitterUser userRecordFromDB = twitterService.getUser(username);
        if(userRecordFromDB != null){
            twitterUserCache.add(username, userRecordFromDB);
        }
        return userRecordFromDB;
    }

    @GetMapping("/users/commonfollowers")
    public List<String> getCommonFollowers(@RequestParam("user1") String username1, @RequestParam("user2") String username2){
        return twitterService.getCommonFollowers(username1, username2);
    }

    @GetMapping("/tweets/keyword")
    public List<String> searchTweetsByKeyword(@RequestParam("query") String query) {
        return twitterService.searchTweetsByKeyword(query);
    }

    @GetMapping("/tweets/timerange")
    public List<String> searchTweetsByTimeRange(@RequestParam("start") Date sdate, @RequestParam("end") Date edate, @RequestParam("keyword") String keyword){
        return twitterService.searchTweetsByTimeRange(sdate, edate, keyword);
    }
}
