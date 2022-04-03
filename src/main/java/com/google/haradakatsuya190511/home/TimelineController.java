package com.google.haradakatsuya190511.home;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.haradakatsuya190511.Tweet;
import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.TweetRepository;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class TimelineController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@RequestMapping("/timeline")
	public ModelAndView timeline(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			List<User> users = new ArrayList<User>();
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			
			for (User usr : user.getFollowingList()) {
				users.add(usr);
			}
			users.add(user);
			
			int[] userIds = new int[users.size()];
			for (int i = 0; i < users.size(); i++) {
				userIds[i] = users.get(i).getId();
			}
			
			List<Tweet> tweets = tweetRepository.findByUserIdInOrderByCreatedAtDesc(userIds);
			mav.addObject("tweets", tweets);
			mav.addObject("user", user);
			mav.setViewName("home/timeline");
			return mav;
		}
	}
}
