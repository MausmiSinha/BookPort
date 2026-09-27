package com.example.BookPort.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BookPort.auth.entity.RegistrationRequestStageView;

@Repository
public interface RegistrationRequestStageViewRepo extends JpaRepository<RegistrationRequestStageView, String>{

	
	RegistrationRequestStageView findByUsername(String username);

    RegistrationRequestStageView findByEmail(String email);

    RegistrationRequestStageView findByRegistrationId(String registrationId);
    
	@NativeQuery("Select gov_id_front from bpvw_registraion_request_stage where registration_id= :id")
	byte[] findGovIdFront(@Param("id") String id);

	@NativeQuery("Select gov_id_back from bpvw_registraion_request_stage where registration_id= :id")
	byte[] findGovIdBack(@Param("id") String id);

	@NativeQuery("Select photo from bpvw_registraion_request_stage where registration_id= :id")
	byte[] findPassportPhoto(@Param("id") String id);

	@NativeQuery("Select * from bpvw_registraion_request_stage  where status= 'PENDING'")
	List<RegistrationRequestStageView> findByStatus();
	
	

    
}
