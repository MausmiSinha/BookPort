package com.example.BookPort.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.RegistrationRequestAudit;

@Repository
public interface RegistrationRequestAuditRepo extends JpaRepository<RegistrationRequestAudit, String> {

//	RegistrationRequestAudit findUserByUsername(String username);
//	
//	
//	@NativeQuery("Select * from registration_request_stagegate where status= 'PENDING'")
//	List<RegistrationRequestAudit> findByStatus();
//
//
	@NativeQuery("Select gov_id_front from bptb_registration_request_audit where registration_id= :id")
	byte[] findGovIdFront(@Param("id") String id);

	@NativeQuery("Select gov_id_back from bptb_registration_request_audit where registration_id= :id")
	byte[] findGovIdBack(@Param("id") String id);

	@NativeQuery("SELECT * FROM bptb_registration_request_audit WHERE registration_id = :id ORDER BY version_no DESC LIMIT 1;")
	RegistrationRequestAudit findFirstByIdRegistrationIdOrderByIdVersionNoDesc(
			@Param("id") String registrationId);
		
}
