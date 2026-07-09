USE book_to;
DROP TABLE IF EXISTS stars;


CREATE TABLE stars (
                           starer INT NOT NULL,
                           starred INT NOT NULL,
                           PRIMARY KEY (starer, starred),

                           CONSTRAINT fk_starer_is_user
                               FOREIGN KEY (starer) REFERENCES users(user_id)
                                   ON DELETE CASCADE,


                           CONSTRAINT fk_starred_is_service
                               FOREIGN KEY (starred) REFERENCES services(service_id)
                                   ON DELETE CASCADE
);