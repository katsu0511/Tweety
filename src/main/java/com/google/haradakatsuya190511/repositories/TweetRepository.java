package com.google.haradakatsuya190511.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.haradakatsuya190511.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
	public Tweet findById(int id);
	public List<Tweet> findByUserIdInOrderByCreatedAtDesc(int[] userIds);
}
