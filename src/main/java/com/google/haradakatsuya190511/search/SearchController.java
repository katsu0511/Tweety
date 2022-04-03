package com.google.haradakatsuya190511.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.haradakatsuya190511.Profile;
import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class SearchController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping("/search")
	public ModelAndView search(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			mav.setViewName("search/search");
			return mav;
		}
	}
	
	@GetMapping("/search_result")
	public void searchResult(@RequestParam String search_word, HttpServletResponse response) throws IOException {
		// レスポンス用JSON文字列生成
		String resData = "{\"users\":[";
		List<User> users = new ArrayList<User>();
		
		if (!search_word.equals("") && search_word != null) {
			List<Map<String, Object>> userIds = new ArrayList<Map<String, Object>>();
			
			try {
				String sql = "SELECT id FROM ("
						   + "SELECT user.id,user_name,name "
						   + "FROM user "
						   + "LEFT JOIN profile "
						   + "ON user.id = profile.user_id) as users "
						   + "WHERE user_name LIKE '%" + search_word + "%' "
						   + "OR name LIKE '%" + search_word + "%' "
						   + "ORDER BY id ASC";
				userIds = jdbcTemplate.queryForList(sql);
	        } catch (EmptyResultDataAccessException e) {
	        	e.printStackTrace();
	        }
			
			for (int i = 0; i < userIds.size(); i++) {
				int id = (int) userIds.get(i).get("id");
				users.add(userRepository.findById(id));
			}
			
			for (int i = 0; i < users.size(); i++) {
				String userName = users.get(i).getUserName();
				Profile profile = users.get(i).getProfile();
				
				String name = null;
				String icon = null;
				if (profile != null) {
					name = profile.getName();
					icon = profile.getIcon();
				}
				
				String userNameStr = userName == null ? "{\"userName\":null," : "{\"userName\":\"" + userName + "\",";
				String nameStr = name == null ? "\"name\":null," : "\"name\":\"" + name + "\",";
				String iconStr = icon == null ? "\"icon\":null}" : "\"icon\":\"" + icon + "\"}";
				
				String userInfo = userNameStr + nameStr + iconStr;
				if (i < users.size() - 1) {
					userInfo += ",";
				}
				
				resData += userInfo;
			}
		}
		
		resData += "]}";

		// レスポンス処理
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf8");
		PrintWriter out = response.getWriter();
		out.println(resData);
	}
}
