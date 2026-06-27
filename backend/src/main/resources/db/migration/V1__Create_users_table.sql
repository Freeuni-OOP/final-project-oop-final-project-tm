USE book_to;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       about_me TEXT,
                       picture_url TEXT,

    -- New columns for email verification
                       is_enabled BOOLEAN DEFAULT FALSE,
                       verification_code VARCHAR(6) DEFAULT NULL
);