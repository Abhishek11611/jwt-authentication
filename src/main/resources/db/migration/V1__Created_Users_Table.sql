CREATE TABLE user_table (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255),
    user_password VARCHAR(255),
    user_email VARCHAR(255),
    user_mobile_number VARCHAR(255),
    otp VARCHAR(255),
    otp_attempts INT,
    otp_expired_at TIMESTAMP,
    user_lock_time TIMESTAMP
);