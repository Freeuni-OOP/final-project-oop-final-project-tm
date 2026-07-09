DROP TABLE IF EXISTS favorites;

CREATE TABLE favorites (
                           favorite_id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           service_id INT NOT NULL,
                           creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_favorites_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                           CONSTRAINT fk_favorites_service FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE CASCADE,
                           UNIQUE(user_id, service_id)
);