DROP TABLE IF EXISTS slots;

CREATE TABLE slots (
                       slot_id INT AUTO_INCREMENT PRIMARY KEY,
                       service_id INT NOT NULL,
                       start_time TIMESTAMP NOT NULL,
                       end_time TIMESTAMP NOT NULL,

                       CONSTRAINT fk_slots_services
                       FOREIGN KEY (service_id)
                       REFERENCES services(service_id)
                       ON DELETE CASCADE
);