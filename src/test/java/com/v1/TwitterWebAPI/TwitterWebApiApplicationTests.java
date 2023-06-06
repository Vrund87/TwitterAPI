package com.v1.TwitterWebAPI;

import com.v1.TwitterWebAPI.controllers.TwitterController;
import com.v1.TwitterWebAPI.dataCache.CacheTwitter;
import com.v1.TwitterWebAPI.models.TwitterUser;
import com.v1.TwitterWebAPI.repositories.UserRepo;
import com.v1.TwitterWebAPI.services.TwitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TwitterWebApiApplicationTests {
	@Mock
	private UserRepo userRepo;

	@Mock
	private CacheTwitter<TwitterUser> twitterUserCache;

	@Mock
	private TwitterService twitterService;

	@InjectMocks
	private TwitterController twitterController;

	@Test
	public void testGetUser() {
		// Prepare test data
		String username = "John";
		List<String> followers = Arrays.asList(
				"Alice", "Bob", "Charlie", "David", "Eve",
				"Frank", "Grace", "Hannah", "Ivy", "Jack"
		);
		TwitterUser expectedUser = new TwitterUser(username, "Lorem ipsum dolor sit amet.", followers);

		when(twitterService.getUser(username)).thenReturn(expectedUser);
		TwitterUser actualUser = twitterController.getUser(username);

		// Verify the result
		assertEquals(username, actualUser.getName());
		assertEquals("Lorem ipsum dolor sit amet.", actualUser.getBio());
		assertEquals(followers, actualUser.getfollowers());

		verify(twitterService, times(1)).getUser(username);
	}
}
