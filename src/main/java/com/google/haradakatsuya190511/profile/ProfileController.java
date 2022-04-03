package com.google.haradakatsuya190511.profile;

//import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.Protocol;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.haradakatsuya190511.Profile;
import com.google.haradakatsuya190511.Tweet;
import com.google.haradakatsuya190511.User;
import com.google.haradakatsuya190511.repositories.ProfileRepository;
import com.google.haradakatsuya190511.repositories.TweetRepository;
import com.google.haradakatsuya190511.repositories.UserRepository;

@Controller
public class ProfileController {
	@Autowired
	HttpSession session;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
//	private static final String ACCESS_KEY   = "";
//	private static final String SECRET_KEY   = "";
//	private static final String ENDPOINT_URL = "s3.us-east-1.amazonaws.com";
//	private static final String REGION       = "";
//	private static final String BUCKET_NAME  = "";
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			List<Tweet> tweets = user.getTweets();
			int numberOfFollowing = user.getFollowingList().size();
			int numberOfFollower = user.getFollowerList().size();
			
			Profile profile = user.getProfile();
			if (profile != null) {
				mav.addObject("profile", profile);
			}
			
			mav.addObject("user", user);
			mav.addObject("tweets", tweets);
			mav.addObject("following", numberOfFollowing);
			mav.addObject("follower", numberOfFollower);
			mav.setViewName("profile/show_profile");
			return mav;
		}
	}
	
	@RequestMapping(value = "/profile/like", method = RequestMethod.GET)
	public ModelAndView profile_like(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			List<Tweet> likes = user.getLikeList();
			int numberOfFollowing = user.getFollowingList().size();
			int numberOfFollower = user.getFollowerList().size();
			
			Profile profile = user.getProfile();
			if (profile != null) {
				mav.addObject("profile", profile);
			}
			
			mav.addObject("user", user);
			mav.addObject("likes", likes);
			mav.addObject("following", numberOfFollowing);
			mav.addObject("follower", numberOfFollower);
			mav.setViewName("profile/show_profile_like");
			return mav;
		}
	}
	
	@RequestMapping(value = "/create_profile", method = RequestMethod.GET)
	public ModelAndView createProfile(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			mav.setViewName("profile/create_profile");
			return mav;
		}
	}
	
	@RequestMapping(value = "/create_profile", method = RequestMethod.POST)
	public ModelAndView createProfile(@RequestParam String name, @RequestParam String profile) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			Profile profileEn = new Profile();
			
			name = name.trim().equals("") ? null : name;
			profile = profile.trim().equals("") ? null : profile;
			
			profileEn.setUser(user);
			profileEn.setName(name);
			profileEn.setProfile(profile);
			profileRepository.save(profileEn);
			
			ModelAndView mav = new ModelAndView("redirect:/profile");
			return mav;
		}
	}
	
	@RequestMapping(value = "/edit_profile", method = RequestMethod.GET)
	public ModelAndView editProfile(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			Profile profile = userRepository.findByUserName(user_name).getProfile();
			mav.setViewName("profile/edit_profile");
			mav.addObject("profile", profile);
			return mav;
		}
	}
	
	@RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
	public ModelAndView editProfile(@RequestParam String name, @RequestParam String profile) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			Profile profileEn = user.getProfile();
			
			name = name.trim().equals("") ? null : name;
			profile = profile.trim().equals("") ? null : profile;
			
			profileEn.setName(name);
			profileEn.setProfile(profile);
			profileRepository.save(profileEn);
			
			ModelAndView mav = new ModelAndView("redirect:/profile");
			return mav;
		}
	}
	
	@RequestMapping(value = "/profile/icon_set", method = RequestMethod.GET)
	public ModelAndView profileIconSet(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			Profile profile = user.getProfile();
			
			if (profile == null || profile.getIcon() == null) {
				mav.addObject("icon", "unset");
			} else {
				mav.addObject("icon", "set");
			}
			
			mav.setViewName("profile/set_icon");
			return mav;
		}
	}
	
	@RequestMapping(value = "/profile/icon_set", method = RequestMethod.POST)
	public ModelAndView profileIconSet(@RequestParam("icon") MultipartFile icon) throws Exception {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav = new ModelAndView("redirect:/login");
			return mav;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			Profile profile;
			String iconPath = "icon/";
			
			if (user.getProfile() == null) {
				profile = new Profile();
				profile.setUser(user);
			} else {
				profile = user.getProfile();
				if (profile.getIcon() != null) {
					// 画像削除
					Path currentIcon = Paths.get(iconPath + profile.getIcon());
					try {
//						AmazonS3 client = getClient(BUCKET_NAME);
//						client.deleteObject(BUCKET_NAME, profile.getIcon());
						Files.delete(currentIcon);
					} catch (IOException e) {
					    e.printStackTrace();
					}
//					catch (AmazonServiceException e) {
//					    e.printStackTrace();
//					}
				}
			}
			
			if (!icon.isEmpty()) {
				// 年月日時の取得
				Date dateObj = new Date();
				SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
				String datetime = format1.format(dateObj);
				
				String iconOriginalName = icon.getOriginalFilename();
				String fileExtention = iconOriginalName.substring(iconOriginalName.lastIndexOf("."), iconOriginalName.length());
				String iconName = user.getId() + "_" + datetime + fileExtention;
				profile.setIcon(iconName);
				
				try {
					// 画像アップロード
		            byte[] bytes = icon.getBytes();
					
					// クライアント生成
//					AmazonS3 client = getClient(BUCKET_NAME);
					
//					InputStream is = icon.getInputStream();
					
//					ObjectMetadata metadata = new ObjectMetadata();
					// 念のためサイズだけセットしておく（不整合ならException）
//					metadata.setContentLength(bytes.length);
					
					// アップロード
//					client.putObject(BUCKET_NAME, iconName, is, metadata);
					
		            BufferedOutputStream stream = new BufferedOutputStream(
		                    new FileOutputStream(new File(iconPath + iconName)));
		            stream.write(bytes);
		            stream.close();
					
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				profile.setIcon(null);
			}
			
			profileRepository.save(profile);
			ModelAndView mav = new ModelAndView("redirect:/profile");
			return mav;
		}
	}
	
	@RequestMapping(value = "/profile/following", method = RequestMethod.GET)
	public ModelAndView profile_following(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			List<User> followings = user.getFollowingList();
			
			mav.addObject("followings", followings);
			mav.setViewName("user/show_followings");
			return mav;
		}
	}
	
	@RequestMapping(value = "/profile/follower", method = RequestMethod.GET)
	public ModelAndView profile_follower(ModelAndView mav) {
		if (session.getAttribute("user_name") == null || session.getAttribute("password") == null) {
			ModelAndView mav2 = new ModelAndView("redirect:/login");
			return mav2;
		} else {
			String user_name = (String) session.getAttribute("user_name");
			User user = userRepository.findByUserName(user_name);
			List<User> followers = user.getFollowerList();
			
			mav.addObject("followers", followers);
			mav.setViewName("user/show_followers");
			return mav;
		}
	}
	
	//--------------------------------------------------
	// クライアント生成
	//--------------------------------------------------
//	private AmazonS3 getClient(String bucketName) throws Exception {
//		
//		// 認証情報
//		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//		
//		// クライアント設定
//		ClientConfiguration clientConfig = new ClientConfiguration();
//		clientConfig.setProtocol(Protocol.HTTPS);  // プロトコル
//		clientConfig.setConnectionTimeout(10000);   // 接続タイムアウト(ms) 
//		
//		// エンドポイント設定
//		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(ENDPOINT_URL, REGION);
//		
//		// クライアント生成
//		AmazonS3 client = AmazonS3ClientBuilder.standard()
//				.withCredentials(new AWSStaticCredentialsProvider(credentials))
//				.withClientConfiguration(clientConfig)
//				.withEndpointConfiguration(endpointConfiguration).build();
//		
//		return client;
//	}
}
