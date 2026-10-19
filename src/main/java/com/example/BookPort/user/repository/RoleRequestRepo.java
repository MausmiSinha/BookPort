package com.example.BookPort.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BookPort.common.dto.OprationEnum;
import com.example.BookPort.common.dto.RoleEnum;
import com.example.BookPort.common.dto.StatusEnum;
import com.example.BookPort.user.entity.RoleRequest;

@Repository
public interface RoleRequestRepo extends JpaRepository<RoleRequest, String>{

//	@Query(value = "SELECT * FROM role_request WHERE user_id = :user_id  AND role = :role", nativeQuery = true)
//	Optional<RoleRequest> findByUserIdAndRole(@Param("user_id") String userId, @Param("role") RoleEnum role);
	
	 boolean existsByUserIdAndRoleAndOperationAndStatus(String userId,RoleEnum role,OprationEnum operation,StatusEnum status);

}
