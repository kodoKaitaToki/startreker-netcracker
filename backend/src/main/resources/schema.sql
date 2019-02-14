CREATE TABLE usr
(
  user_id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name          VARCHAR(64)  not null,
  user_password      VARCHAR(128) not null,
  user_email         VARCHAR(128) not null,
  user_refresh_token VARCHAR(128),
  user_is_activated BOOLEAN
);

CREATE TABLE role
(
  role_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(64) not null
);

CREATE TABLE assigned_role
(
  user_id BIGINT REFERENCES usr (user_id),
  role_id BIGINT REFERENCES role (role_id)
);