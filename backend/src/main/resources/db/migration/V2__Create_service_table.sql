USE book_to;
DROP table IF EXISTS services;

CREATE TABLE services (
                    service_id INT AUTO_INCREMENT PRIMARY KEY,
                    provider_id INT NOT NULL,
                    title TEXT NOT NULL,
                    bio TEXT,
                    picture_url TEXT,
                    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                    CONSTRAINT fk_provider_with_service
                    FOREIGN KEY (provider_id) REFERENCES users(user_id)
                    ON DELETE CASCADE
                    /*
                    ON DELETE CASCADE means that in case a provider chooses to delete their account,
                    the services associated with that account will also be deleted
                    */
);