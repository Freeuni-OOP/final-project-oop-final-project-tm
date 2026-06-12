-- ─────────────────────────────────────────────────────────────────────────────
-- V2 – Booking Slots Table
-- Stores all schedulable time slots for the clinic calendar.
-- Status values: 'FREE' | 'PENDING' | 'BOOKED'
-- ─────────────────────────────────────────────────────────────────────────────

CREATE TABLE booking_slots (
    id             BIGINT       AUTO_INCREMENT PRIMARY KEY,
    slot_date_time DATETIME     NOT NULL               COMMENT 'Exact start date/time of this slot',
    status         VARCHAR(20)  NOT NULL DEFAULT 'FREE' COMMENT 'FREE | PENDING | BOOKED',
    client_name    VARCHAR(100)                         COMMENT 'Name of the requesting client (NULL if FREE)',
    client_email   VARCHAR(100)                         COMMENT 'Email of the requesting client (NULL if FREE)',
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_slot_status (status),
    INDEX idx_slot_date   (slot_date_time)
);
