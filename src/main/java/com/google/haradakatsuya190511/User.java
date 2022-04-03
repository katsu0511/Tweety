package com.google.haradakatsuya190511;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "user_name", nullable = false, length = 32, unique = true)
	private String userName;
	
	@Column(name = "password", nullable = false, length = 32)
	private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Profile profile;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Tweet> tweets;
	
	@ManyToMany(mappedBy = "likerList")
	private List<Tweet> likeList;
	
	@ManyToMany
	@JoinTable(name="follow",
			   joinColumns = @JoinColumn(name = "followed_id"),
			   inverseJoinColumns = @JoinColumn(name="following_id"))
	private List<User> followerList;
	
	@ManyToMany(mappedBy = "followerList")
	private List<User> followingList;
	
	public User() {}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public List<Tweet> getTweets() {
		return tweets;
	}
	
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	public List<Tweet> getLikeList() {
		return likeList;
	}
	
	public void setLikeList(List<Tweet> likeList) {
		this.likeList = likeList;
	}
	
	public List<User> getFollowerList() {
		return followerList;
	}
	
	public void setFollowerList(List<User> followerList) {
		this.followerList = followerList;
	}
	
	public List<User> getFollowingList() {
		return followingList;
	}
	
	public void setFollowingList(List<User> followingList) {
		this.followingList = followingList;
	}
}
