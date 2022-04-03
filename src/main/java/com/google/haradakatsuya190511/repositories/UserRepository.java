package com.google.haradakatsuya190511.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.haradakatsuya190511.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUserNameAndPassword(String userName, String password);
	public User findByUserName(String userName);
	public User findById(int id);
}
