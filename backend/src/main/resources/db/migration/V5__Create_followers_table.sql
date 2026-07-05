USE book_to;
DROP TABLE IF EXISTS followers;


CREATE TABLE followers (
                          follower INT NOT NULL,
                          following INT NOT NULL,
                          PRIMARY KEY (follower, following),

                          CONSTRAINT fk_follower_is_user
                              FOREIGN KEY (follower) REFERENCES users(user_id)
                                  ON DELETE CASCADE,


                          CONSTRAINT fk_taker_with_service
                              FOREIGN KEY (following) REFERENCES users(user_id)
                                  ON DELETE CASCADE
);