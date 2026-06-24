CREATE TABLE booking_slots (
    id             BIGINT       AUTO_INCREMENT PRIMARY KEY,
    service_id     INT          NOT NULL,
    slot_date_time DATETIME     NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'FREE',
    client_name    VARCHAR(100),
    client_email   VARCHAR(100),
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_slot_status (status),
    INDEX idx_slot_date   (slot_date_time)
);