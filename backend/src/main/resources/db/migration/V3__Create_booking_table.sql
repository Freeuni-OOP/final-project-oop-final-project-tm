USE book_to;
DROP TABLE IF EXISTS bookings;


CREATE TABLE bookings (
    taker_id INT NOT NULL,
    service_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (taker_id, service_id),

    CONSTRAINT fk_service_with_booking
    FOREIGN KEY (service_id) REFERENCES services(service_id)
    ON DELETE CASCADE,


    CONSTRAINT fk_taker_with_service
    FOREIGN KEY (taker_id) REFERENCES users(user_id)
    ON DELETE CASCADE
);