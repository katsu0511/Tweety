package com.google.haradakatsuya190511.home;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class LoginController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository repository;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			ModelAndView mav,
			@CookieValue(name = "user_name", required = false, defaultValue = "") String cookieUserName,
			@CookieValue(name = "password", required = false, defaultValue = "") String cookiePassword) {
		
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			mav.setViewName("home/login");
			mav.addObject("user_name", cookieUserName);
			mav.addObject("password", cookiePassword);
			return mav;
		} else {
			return new ModelAndView("redirect:/timeline");
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(
			@RequestParam String user_name,
			@RequestParam String password,
			ModelAndView mav,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			if (user_name.equals("") || password.equals("")) {
				mav.setViewName("home/login");
				mav.addObject("msg", "ユーザー名とパスワードを入力してください");
				mav.addObject("user_name", user_name);
				mav.addObject("password", password);
				return mav;
			} else {
				User user = repository.findByUserNameAndPassword(user_name, password);
				
				if (user != null) {
					session.setAttribute("user_name", user_name);
					session.setAttribute("password", password);
					user_name = URLEncoder.encode(user_name, StandardCharsets.UTF_8);
					password = URLEncoder.encode(password, StandardCharsets.UTF_8);
					
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
						cookiePassword.setValue(password);
					} else {
						cookiePassword = new Cookie("password", password);
					}
					
					response.addCookie(cookieUserName);
					response.addCookie(cookiePassword);
					return new ModelAndView("redirect:/profile");
				} else {
					mav.setViewName("home/login");
					mav.addObject("msg", "ユーザー名かパスワードが間違っています");
					mav.addObject("user_name", user_name);
					mav.addObject("password", password);
					return mav;
				}
			}
		} else {
			return new ModelAndView("redirect:/timeline");
		}
	}
}
