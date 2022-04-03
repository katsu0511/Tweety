package com.google.haradakatsuya190511.tweet;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.haradakatsuya190511.Tweet;
import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.TweetRepository;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class TweetController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@GetMapping("/tweet/{id}")
	public ModelAndView tweet(@PathVariable int id, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			Tweet tweet = tweetRepository.findById(id);
			
			mav.setViewName("tweet/show_tweet");
			mav.addObject("user", user);
			mav.addObject("tweet", tweet);
			return mav;
		}
	}
	
	@GetMapping("/create_tweet")
	public ModelAndView createTweet(ModelAndView mav) {
    	if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
    		ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			mav.setViewName("tweet/create_tweet");
			mav.addObject("user", user);
			return mav;
		}
	}
	
	@PostMapping("/create_tweet")
	public ModelAndView createTweet(@RequestParam int user_id, @RequestParam String tweet) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			User user = userRepository.findById(user_id);
			Tweet tweetEn = new Tweet();
			tweetEn.setUser(user);
			tweetEn.setContent(tweet);
			tweetRepository.saveAndFlush(tweetEn);
			
			ModelAndView mav = new ModelAndView("redirect:/timeline");
			return mav;
		}
	}
	
	@PostMapping("/delete_tweet/{id}")
	public ModelAndView deleteTweet(@PathVariable int id) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			Tweet tweet = tweetRepository.findById(id);
			tweetRepository.delete(tweet);
			
			ModelAndView mav = new ModelAndView("redirect:/timeline");
			return mav;
		}
	}
	
	@RequestMapping("/tweet/{id}/like")
	public ModelAndView tweet_like(@PathVariable int id, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			Tweet tweet = tweetRepository.findById(id);
			List<User> likers = tweet.getLikerList();
			
			if (likers.size() == 0) {
				ModelAndView mav2 = new ModelAndView("redirect:/tweet/" + id);
				return mav2;
			} else {
				mav.addObject("likers", likers);
				mav.setViewName("tweet/show_likers");
				return mav;
			}
		}
	}
}
