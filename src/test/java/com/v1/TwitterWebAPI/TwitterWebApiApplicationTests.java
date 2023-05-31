package com.v1.TwitterWebAPI;

import com.v1.TwitterWebAPI.controllers.TwitterController;
import com.v1.TwitterWebAPI.models.TwitterUser;
import com.v1.TwitterWebAPI.services.TwitterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class TwitterWebApiApplicationTests {

//	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TwitterService twitterService;

	@Autowired
	private TwitterController twitterController;

	@Test
	void contextLoads() {
	}

//	@Test
//	void getAllUsersTest() throws Exception {
//		List<TwitterUser> users = new ArrayList<>();
//		users.add(new TwitterUser("user1", "Bio 1", Arrays.asList("follower1", "follower2")));
//		users.add(new TwitterUser("user2", "Bio 2", Arrays.asList("follower3", "follower4")));
//
//		when(twitterService.getAllUsers()).thenReturn(users);
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
//				.andDo(print())
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("user1"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[0].bio").value("Bio 1"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[0].followers[0]").value("follower1"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[0].followers[1]").value("follower2"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("user2"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[1].bio").value("Bio 2"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[1].followers[0]").value("follower3"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$[1].followers[1]").value("follower4"));
//	}

	@Test
	void getUserTest() throws Exception {
		TwitterUser user = new TwitterUser("user1", "Bio 1", Arrays.asList("follower1", "follower2"));

		when(twitterService.getUser("user1")).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", "user1"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Bio 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.followers[0]").value("follower1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.followers[1]").value("follower2"));
	}

	@Test
	void getCommonFollowersTest() throws Exception {
		List<String> commonFollowers = Arrays.asList("follower1", "follower2", "follower5");

		when(twitterService.getCommonFollowers("user1", "user2")).thenReturn(commonFollowers);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
						.param("user1", "user1")
						.param("user2", "user2"))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").value("follower1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1]").value("follower2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2]").value("follower5"));
	}
}
