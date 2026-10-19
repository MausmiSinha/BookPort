/*Database Creation*/
CREATE DATABASE IF NOT EXISTS library_management;

USE library_management;
/*****************************************************************************************************************************************************/

/*Table Creation*/
CREATE TABLE bptb_registration_request_audit (
  version_no INT NOT NULL DEFAULT 1,
  registration_id VARCHAR(19) NOT NULL ,
  username VARCHAR(100) NOT NULL,
  name VARCHAR(500) NOT NULL,
  email VARCHAR(100) NOT NULL,
  mobile_number VARCHAR(13),
  gender ENUM('MALE', 'FEMALE', 'OTHERS') NOT NULL,
  dob DATE NOT NULL,
  status ENUM('PENDING', 'APPROVED', 'REJECTED', 'INFO_REQUIRED') NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  password_hash VARCHAR(255) NOT NULL,
  gov_id_front LONGBLOB NOT NULL,
  gov_id_back LONGBLOB NOT NULL,
  photo LONGBLOB NOT NULL,
  processing_status ENUM('HOLD', 'FAILED', 'COMPLETED') NOT NULL DEFAULT 'HOLD',
  remark VARCHAR(500),
  updated_by VARCHAR(100) NOT NULL,
  
  PRIMARY KEY (version_no, registration_id, username, email)
);

CREATE TABLE core_user_details (
  user_id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(500) NOT NULL,
  email VARCHAR(100) NOT NULL,
  mobile_number VARCHAR(10),
  gender VARCHAR(20),
  dob DATE,
  status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT uk_core_user_email UNIQUE (email),
  CONSTRAINT uk_core_user_mobile UNIQUE (mobile_number)
);

ALTER TABLE core_user_details
DROP INDEX uk_core_user_mobile,
MODIFY mobile_number VARCHAR(10) NOT NULL;

CREATE TABLE auth_user_details (
  user_id VARCHAR(36) PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
  account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
  credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  failed_attempts INT NOT NULL DEFAULT 0,
  last_login_at TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT uk_auth_username UNIQUE (username)
);

select * from roles;
CREATE TABLE roles (
  role_id VARCHAR(19) PRIMARY KEY,
  role_name VARCHAR(100) NOT NULL,

  CONSTRAINT uk_role_name UNIQUE (role_name)
);

CREATE TABLE user_roles (
  user_id VARCHAR(19) NOT NULL,
  role_id VARCHAR(19) NOT NULL,

  PRIMARY KEY (user_id, role_id),

  CONSTRAINT fk_user_roles_auth_user
    FOREIGN KEY (user_id)
    REFERENCES auth_user_details(user_id),

  CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
);

CREATE TABLE password_history (
  password_history_id VARCHAR(36) PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  old_password VARCHAR(255) NOT NULL,
  latest_password VARCHAR(255) NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_reverted BOOLEAN NOT NULL DEFAULT FALSE,
  remark VARCHAR(500),
  CONSTRAINT fk_password_history_auth_user
    FOREIGN KEY (user_id)
    REFERENCES auth_user_details(user_id)
);

CREATE TABLE role_request (
    request_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    username VARCHAR(100) NOT NULL,
	name VARCHAR(500) NOT NULL,
    user_email VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL, -- Role can be 'ADMIN', 'LIBRARIAN'
    operation VARCHAR(20) NOT NULL, -- Operation defines: GRANT, REVOKE
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    remark VARCHAR(500),
    requested_by VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    processed_at TIMESTAMP NULL
);

ALTER TABLE role_request
DROP COLUMN processed_at;

Alter table role_request 
add column updated_by VARCHAR(36) NOT NULL;

CREATE TABLE email_notification_logs (
	notification_id VARCHAR(36) PRIMARY KEY,
    producer_module VARCHAR(100) NOT NULL,
    receiver_email VARCHAR(150) NOT NULL,
    email_type VARCHAR(200) NOT NULL,
    receiver_user_id VARCHAR(25) NOT NULL,
    email_subject VARCHAR(200) NOT NULL,
    email_message LONGBLOB,
    notif_status VARCHAR(200),
    retry_count INT(1) Default 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


/*****************************************************************************************************************************************************/

/*Describe Table*/
show tables;
/*****************************************************************************************************************************************************/

/*Static Data*/
-- Roles
insert into roles values('262040108146104999', 'ADMIN');
insert into roles values('262040108146104101', 'USER');
-- user_roles 
insert into user_roles values('2619902065934187101', '262040108146104999'); -- for admin

/*****************************************************************************************************************************************************/


/*View Creation*/
CREATE VIEW bpvw_registraion_request_stage AS
SELECT
    r.registration_id,
    r.username,
    r.name,
    r.email,
    r.mobile_number,
    r.gender,
    r.dob,
    r.status,
    r.created_at,
    r.updated_at,
    r.password_hash,
    r.gov_id_front,
    r.gov_id_back,
    r.photo,
    r.processing_status,
    r.remark
FROM bptb_registration_request_audit r
INNER JOIN (
    SELECT
        registration_id,
        MAX(version_no) AS latest_version
    FROM bptb_registration_request_audit
    GROUP BY registration_id
) latest
ON r.registration_id = latest.registration_id
AND r.version_no = latest.latest_version;
/*****************************************************************************************************************************************************/


/*Drop Table*/

/*****************************************************************************************************************************************************/

/*Queries Creation*/
select * from core_user_details where user_id='2626102073644399000';
select * from auth_user_details where user_id='2619902048038711000';
select * from roles;
select * from role_request where  user_id='2625602044715684000';
select * from email_notification_logs;
Select * from user_roles u, roles r where u.user_id='2619902050081003000' and u.role_id=r.role_id;
Select * from user_roles where user_id='2625502047908183000';
select * from bpvw_registraion_request_stage where registration_id='2626101073607200000';
select * from bptb_registration_request_audit where registration_id='2625501048246265000';	
select * from password_history;
Select * from email_notification_logs;
/*****************************************************************************************************************************************************/
