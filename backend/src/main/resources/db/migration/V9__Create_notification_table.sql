USE book_to;
DROP TABLE IF EXISTS notifications;

CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    txt TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    seen BIT NOT NULL,

    CONSTRAINT fk_notif_with_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
)