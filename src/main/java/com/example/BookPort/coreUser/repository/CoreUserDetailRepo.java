package com.example.BookPort.coreUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookPort.coreUser.entity.CoreUserDetail;

@Repository
public interface CoreUserDetailRepo extends JpaRepository<CoreUserDetail, String>{

	CoreUserDetail findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
}
