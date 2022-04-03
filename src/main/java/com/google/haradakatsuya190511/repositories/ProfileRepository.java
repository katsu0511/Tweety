package com.google.haradakatsuya190511.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.haradakatsuya190511.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	public Profile findByUserId(Integer userId);
}
