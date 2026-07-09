USE book_to;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS services;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE services (
                    service_id INT AUTO_INCREMENT PRIMARY KEY,
                    provider_id INT NOT NULL,
                    title TEXT NOT NULL,
                    bio TEXT,
                    image_path TEXT,
                    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    price DECIMAL(20, 4) NOT NULL,
                    category TEXT,
                    address TEXT,
                    max_capacity INT DEFAULT 1,

                    CONSTRAINT fk_provider_with_service
                    FOREIGN KEY (provider_id) REFERENCES users(user_id)
                    ON DELETE CASCADE
                    /*
                    ON DELETE CASCADE means that in case a provider chooses to delete their account,
                    the services associated with that account will also be deleted
                    */
);