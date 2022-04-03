package com.google.haradakatsuya190511.user;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.haradakatsuya190511.Profile;
import com.google.haradakatsuya190511.Tweet;
import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class UserController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository repository;
	
	@RequestMapping("/user/{userName}")
	public ModelAndView show_user(@PathVariable String userName, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String login_user_name = (String) session.getAttribute("user_name");
			if (userName.equals(login_user_name)) {
				ModelAndView mav2 = new ModelAndView("redirect:/profile");
				return mav2;
			} else {
				User user = repository.findByUserName(userName);
				List<Tweet> tweets = user.getTweets();
				int numberOfFollowing = user.getFollowingList().size();
				int numberOfFollower = user.getFollowerList().size();
				
				Profile profile = user.getProfile();
				if (profile != null) {
					mav.addObject("profile", profile);
				}
				
				User self = repository.findByUserName(login_user_name);
				
				mav.addObject("user", user);
				mav.addObject("tweets", tweets);
				mav.addObject("following", numberOfFollowing);
				mav.addObject("follower", numberOfFollower);
				mav.addObject("self", self);
				mav.setViewName("user/show_user");
				return mav;
			}
		}
	}
	
	@RequestMapping("/user/{userName}/like")
	public ModelAndView show_user_like(@PathVariable String userName, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String login_user_name = (String) session.getAttribute("user_name");
			if (userName.equals(login_user_name)) {
				ModelAndView mav2 = new ModelAndView("redirect:/profile/like");
				return mav2;
			} else {
				User user = repository.findByUserName(userName);
				List<Tweet> likes = user.getLikeList();
				int numberOfFollowing = user.getFollowingList().size();
				int numberOfFollower = user.getFollowerList().size();
				
				Profile profile = user.getProfile();
				if (profile != null) {
					mav.addObject("profile", profile);
				}
				
				User self = repository.findByUserName(login_user_name);
				
				mav.addObject("user", user);
				mav.addObject("likes", likes);
				mav.addObject("following", numberOfFollowing);
				mav.addObject("follower", numberOfFollower);
				mav.addObject("self", self);
				mav.setViewName("user/show_user_like");
				return mav;
			}
		}
	}
	
	@RequestMapping(value = "/create_user", method = RequestMethod.GET)
	public ModelAndView createUser(ModelAndView mav) {
    	if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
    		mav.setViewName("user/create_user");
    		mav.addObject("user_name", "");
			mav.addObject("password", "");
			return mav;
		} else {
			ModelAndView mav2 = new ModelAndView("redirect:/timeline");
			return mav2;
		}
	}
	
	@RequestMapping(value = "/create_user", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView createUser(
			ModelAndView mav,
			@RequestParam String user_name,
			@RequestParam String password2,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			User user_in_db = repository.findByUserName(user_name);
			
			if (user_in_db != null) {
				mav.setViewName("user/create_user");
				mav.addObject("msg", "このユーザーIDはすでに登録されています。");
				mav.addObject("user_name", user_name);
				mav.addObject("password", password2);
				return mav;
			} else {
				User user = new User();
				user.setUserName(user_name);
				user.setPassword(password2);
				repository.save(user);
				session.setAttribute("user_name", user_name);
				session.setAttribute("password", password2);
				
				user_name = URLEncoder.encode(user_name, StandardCharsets.UTF_8);
				password2 = URLEncoder.encode(password2, StandardCharsets.UTF_8);
				
				Cookie[] cookies = request.getCookies();
				Cookie cookieUserName = null;
				Cookie cookiePassword = null;
				
				if (cookies != null) {
					for  (Cookie cookie : cookies) {
						if (cookie.getName().equals("user_name")) {
							cookieUserName = cookie;
						} else if (cookie.getName().equals("password")) {
							cookiePassword = cookie;
						}
					}
				}
				
				if (cookieUserName != null) {
					cookieUserName.setValue(user_name);
				} else {
					cookieUserName = new Cookie("user_name", user_name);
				}
				
				if (cookiePassword != null) {
					cookiePassword.setValue(password2);
				} else {
					cookiePassword = new Cookie("password", password2);
				}
				
				response.addCookie(cookieUserName);
				response.addCookie(cookiePassword);
				return new ModelAndView("redirect:/profile");
			}
		} else {
			ModelAndView mav2 = new ModelAndView("redirect:/timeline");
			return mav2;
		}
	}
	
	@RequestMapping("/user/{userName}/following")
	public ModelAndView show_user_following(@PathVariable String userName, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String login_user_name = (String) session.getAttribute("user_name");
			if (userName.equals(login_user_name)) {
				ModelAndView mav2 = new ModelAndView("redirect:/profile/following");
				return mav2;
			} else {
				User user = repository.findByUserName(userName);
				List<User> followings = user.getFollowingList();
				
				mav.addObject("followings", followings);
				mav.setViewName("user/show_followings");
				return mav;
			}
		}
	}
	
	@RequestMapping("/user/{userName}/follower")
	public ModelAndView show_user_follower(@PathVariable String userName, ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String login_user_name = (String) session.getAttribute("user_name");
			if (userName.equals(login_user_name)) {
				ModelAndView mav2 = new ModelAndView("redirect:/profile/following");
				return mav2;
			} else {
				User user = repository.findByUserName(userName);
				List<User> followers = user.getFollowerList();
				
				mav.addObject("followers", followers);
				mav.setViewName("user/show_followers");
				return mav;
			}
		}
	}
}
