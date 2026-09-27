package com.example.BookPort.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookPort.user.entity.CoreUserDetail;

@Repository
public interface CoreUserDetailRepo extends JpaRepository<CoreUserDetail, String>{

	CoreUserDetail findUserByUsername(String username);
	
	CoreUserDetail findUserByUserId(String userid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
}
