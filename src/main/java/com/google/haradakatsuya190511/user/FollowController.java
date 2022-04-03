package com.google.haradakatsuya190511.user;

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
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class FollowController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public void follow(@RequestParam int id, HttpServletResponse response) {
		try {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			
			jdbcTemplate.update(
				"INSERT INTO follow(followed_id, following_id) VALUES (?, ?)",
				id, user.getId()
			);
        } catch (EmptyResultDataAccessException e) {
        	e.printStackTrace();
        }
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf8");
	}
	
	@RequestMapping(value = "/unfollow", method = RequestMethod.POST)
	public void unfollow(@RequestParam int id, HttpServletResponse response) {
		try {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			
			jdbcTemplate.update(
				"DELETE FROM follow WHERE followed_id=? AND following_id=?",
				id, user.getId()
			);
        } catch (EmptyResultDataAccessException e) {
        	e.printStackTrace();
        }
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf8");
	}
}
