package com.example.BookPort.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, String>{
	
	// Fetches default user role
	@NativeQuery("Select * from roles where role_name= :role_name")
	Role findByRoleName(@Param("role_name") String role_name);
	
//	Role findByRoleName(String roleName);

}
