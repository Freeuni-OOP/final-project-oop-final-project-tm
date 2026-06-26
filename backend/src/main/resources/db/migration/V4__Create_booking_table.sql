
DROP TABLE IF EXISTS bookings;


CREATE TABLE bookings (
    taker_id INT NOT NULL,
    slot_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (taker_id, slot_id),

    CONSTRAINT fk_slot_with_booking
    FOREIGN KEY (slot_id) REFERENCES services(service_id)
    ON DELETE CASCADE,


    CONSTRAINT fk_taker_with_service
    FOREIGN KEY (taker_id) REFERENCES users(user_id)
    ON DELETE CASCADE
);