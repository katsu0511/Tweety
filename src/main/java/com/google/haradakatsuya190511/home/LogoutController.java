package com.google.haradakatsuya190511.home;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController {
	@Autowired
	HttpSession session;
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logout() {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			session.removeAttribute("user_name");
			session.removeAttribute("password");
			return new ModelAndView("redirect:/login");
		}
	}
}
