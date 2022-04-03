package com.google.haradakatsuya190511.tweet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.TweetRepository;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class LikeController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	public void like(@RequestParam int id, HttpServletResponse response) {
		try {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			
			jdbcTemplate.update(
				"INSERT INTO favorite(user_id, tweet_id) VALUES (?, ?)",
				user.getId(), id
			);
        } catch (EmptyResultDataAccessException e) {
        	e.printStackTrace();
        }
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf8");
	}
	
	@RequestMapping(value = "/unlike", method = RequestMethod.POST)
	public void unlike(@RequestParam int id, HttpServletResponse response) {
		try {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			
			jdbcTemplate.update(
				"DELETE FROM favorite WHERE user_id=? AND tweet_id=?",
				user.getId(), id
			);
        } catch (EmptyResultDataAccessException e) {
        	e.printStackTrace();
        }
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf8");
	}
}
