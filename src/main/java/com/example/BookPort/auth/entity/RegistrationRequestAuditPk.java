package com.example.BookPort.auth.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Composite Primary Key class for the bptb_registration_request_audit table.
 *
 * The table uses a composite primary key consisting of:
 *      version_no + registration_id + username + email
 *
 * As per the JPA specification, composite primary keys should be represented
 * using an @Embeddable class and referenced in the entity using @EmbeddedId.
 * This class encapsulates all the primary key fields into a single reusable
 * object and provides equals() and hashCode() implementations required for
 * entity identity and persistence operations.
 */


public class RegistrationRequestAuditPk implements Serializable {
	
	
    private Integer versionNo;

 
    private String registrationId;


    private String username;

    private String email;

    public RegistrationRequestAuditPk() {
    }

    public RegistrationRequestAuditPk(Integer versionNo, String registrationId,
                                      String username, String email) {
        this.versionNo = versionNo;
        this.registrationId = registrationId;
        this.username = username;
        this.email = email;
    }

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof RegistrationRequestAuditPk)) return false;
	        RegistrationRequestAuditPk that = (RegistrationRequestAuditPk) o;
	        return Objects.equals(versionNo, that.versionNo)
	                && Objects.equals(registrationId, that.registrationId)
	                && Objects.equals(username, that.username)
	                && Objects.equals(email, that.email);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(versionNo, registrationId, username, email);
	    }
}
