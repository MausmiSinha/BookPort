CREATE DATABASE IF NOT EXISTS library_management;

USE library_management;

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

CREATE TABLE address_directory (
  user_id VARCHAR(36) PRIMARY KEY,
  building_name VARCHAR(255),
  street_name VARCHAR(255),
  post_code VARCHAR(20),
  town_name VARCHAR(100),
  state VARCHAR(100),
  country VARCHAR(100),

  CONSTRAINT fk_address_user
    FOREIGN KEY (user_id)
    REFERENCES core_user_details(user_id)
);

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
  role_id VARCHAR(36) PRIMARY KEY,
  role_name VARCHAR(100) NOT NULL,

  CONSTRAINT uk_role_name UNIQUE (role_name)
);

CREATE TABLE user_roles (
  user_id VARCHAR(36) NOT NULL,
  role_id VARCHAR(25) NOT NULL,

  PRIMARY KEY (user_id, role_id),

  CONSTRAINT fk_user_roles_auth_user
    FOREIGN KEY (user_id)
    REFERENCES auth_user_details(user_id),

  CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
);
Drop table password_history;


CREATE TABLE password_history (
  password_history_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id VARCHAR(36) NOT NULL,
  old_password VARCHAR(255),
  latest_password VARCHAR(255) NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_reverted BOOLEAN NOT NULL DEFAULT FALSE,

  INDEX idx_password_history_user_id (user_id),

  CONSTRAINT fk_password_history_auth_user
    FOREIGN KEY (user_id)
    REFERENCES auth_user_details(user_id)
);

/*
	Notification Log table
*/
CREATE TABLE email_notification_logs (
	notification_id VARCHAR(36) PRIMARY KEY,
    producer_module VARCHAR(100) NOT NULL,
    receiver_email VARCHAR(150) NOT NULL,
    email_type VARCHAR(200) NOT NULL,
    receiver_user_id VARCHAR(25) NOT NULL,
    email_subject VARCHAR(200) NOT NULL,
    email_message VARCHAR(5000),
    notif_status VARCHAR(200),
    retry_count INT(1) Default 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Select * from email_notification_logs;

SELECT *
FROM password_history
WHERE user_id = "ms1"
ORDER BY updated_at DESC;

CREATE TABLE user_identifier (
  user_id VARCHAR(36) PRIMARY KEY,
  gov_id_front VARCHAR(500),
  gov_id_back VARCHAR(500),

  CONSTRAINT fk_user_identifier_core_user
    FOREIGN KEY (user_id)
    REFERENCES core_user_details(user_id)
);

UPDATE registration_request_stagegate
SET status = 'PENDING'
WHERE status = '0'
AND registration_id IS NOT NULL;

CREATE TABLE registration_request_stagegate (
  registration_id VARCHAR(36) PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  name VARCHAR(500) NOT NULL,
  email VARCHAR(100) NOT NULL,
  mobile_number VARCHAR(20),
  gender VARCHAR(20),
  dob DATE,
  status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  password_hash VARCHAR(255) NOT NULL,
  gov_id_front VARCHAR(500),
  gov_id_back VARCHAR(500),

);

SET SQL_SAFE_UPDATES = 0;

UPDATE registration_request_stagegate
SET status = 'PENDING'
WHERE status = '0';

UPDATE registration_request_stagegate
SET status = 'APPROVED'
WHERE status = '1';

UPDATE registration_request_stagegate
SET status = 'REJECTED'
WHERE status = '2';

ALTER TABLE registration_request_stagegate
DROP CONSTRAINT uk_registration_username,
DROP CONSTRAINT uk_registration_email;

ALTER TABLE registration_request_stagegate
MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
MODIFY COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE registration_request_stagegate
MODIFY COLUMN gov_id_front LONGBLOB,
MODIFY COLUMN gov_id_back LONGBLOB;

select * from registration_request_stagegate;
Delete from registration_request_stagegate where registration_id='2615001052497506000';


CREATE TABLE registration_requests (
  registration_request_id VARCHAR(36) PRIMARY KEY,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  verification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
  remark VARCHAR(500),
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  processing_status VARCHAR(30) NOT NULL DEFAULT 'HOLD',

  CONSTRAINT fk_registration_request_stagegate
    FOREIGN KEY (registration_request_id)
    REFERENCES registration_request_stagegate(registration_id)
);

ALTER TABLE core_user_details
ADD COLUMN username VARCHAR(100) NOT NULL AFTER user_id;

INSERT INTO roles (role_id, role_name)
VALUES ('ROLE_ADMIN_ID', 'ADMIN')
ON DUPLICATE KEY UPDATE role_name = role_name;

INSERT INTO roles (role_id, role_name)
VALUES ('ROLE_USER_ID', 'USER')
ON DUPLICATE KEY UPDATE role_name = role_name;

INSERT INTO core_user_details (
    user_id,
    username,
    name,
    email,
    mobile_number,
    gender,
    dob,
    status
) VALUES (
    'SYSTEM_ADMIN',
    'admin',
    'System Administrator',
    'admin@bookport.local',
    '9999999999',
    'M',
    '2000-01-01',
    'ACTIVE'
);

INSERT INTO auth_user_details (
    user_id,
    username,
    password_hash,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled,
    failed_attempts
) VALUES (
    'SYSTEM_ADMIN',
    'admin',
    '$2a$12$aDrB05zLOBlDe7HVUv/oJ.dAGvYIHBl2I7qQmVZPUZZA7uQ0ebsES',
    true,
    true,
    true,
    true,
    0
);

INSERT INTO user_roles (
    user_id,
    role_id
) VALUES (
    'SYSTEM_ADMIN',
    'ROLE_ADMIN_ID'
);

SELECT * FROM core_user_details WHERE user_id = '2619902048420496000';

Delete FROM core_user_details where user_id = '2619902065348715000';
delete FROM auth_user_details WHERE user_id = '2619902065348715000';
delete FROM user_roles WHERE user_id = '2619902065348715000';
Select * from user_roles;
desc auth_user_details;
SELECT * FROM auth_user_details WHERE user_id = '2619902063855032000';
Select * from email_notification_logs;
SELECT * FROM registration_request_stagegate WHERE username = 'ms1';

delete from registration_request_stagegate WHERE registration_id = '2619901048173731000';

select * from user_roles;