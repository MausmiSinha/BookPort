package com.example.BookPort.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.UserRoles;

@Repository
public interface UserRolesRepo extends JpaRepository<UserRoles, String>{

	Optional<UserRoles> findByUserIdAndRoleId(String userId, String roleId);


}
