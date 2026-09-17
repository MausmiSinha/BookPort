package com.example.BookPort.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.PasswordHistory;

@Repository
public interface PasswordHistoryRepo extends JpaRepository<PasswordHistory, Long>{
	
	 @NativeQuery("""
	            SELECT *
	            FROM password_history
	            WHERE user_id = :userId
	            ORDER BY updated_at DESC
	            LIMIT 3
	            """)
	    List<PasswordHistory> findTop3ByUserIdOrderByUpdatedAtDesc(@Param("userId") String userId);

}
