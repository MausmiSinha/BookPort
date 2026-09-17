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


/*****************************************************************************************************************************************************/

/*Describe Table*/
show tables;
desc registration_request_audit;
/*****************************************************************************************************************************************************/

/*Static Data*/
-- Roles
insert into roles values('262040108146104999', 'ADMIN');
insert into roles values('262040108146104101', 'USER');
select * from roles;
select * from user_roles;
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
-- DROP table bptb_registration_request_audit;
/*****************************************************************************************************************************************************/


/*Queries Creation*/
select * from core_user_details;
desc core_user_details;
select * from auth_user_details;
select * from roles;
Select * from user_roles;
Select * from registration_request_stagegate;
select * from bpvw_registraion_request_stage where status='PENDING';
select * from bptb_registration_request_audit where registration_id='2624801058786029000';
/*****************************************************************************************************************************************************/


