package com.example.BookPort.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.AuthUserDetails;

@Repository
public interface AuthUserDetailsRepo extends JpaRepository<AuthUserDetails, String>{
	
	AuthUserDetails findAuthUserDetailsByUsername(String username);
	
	@Query(value = """
            SELECT r.role_name
            FROM auth_user_details aud
            JOIN user_roles ur ON aud.user_id = ur.user_id
            JOIN roles r ON ur.role_id = r.role_id
            WHERE aud.username = :username
            """, nativeQuery = true)
    List<String> findRoleNamesByUsername(String username);
	
	boolean existsByUsername(String username);

}
