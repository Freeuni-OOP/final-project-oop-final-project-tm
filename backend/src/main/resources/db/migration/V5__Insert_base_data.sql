INSERT INTO users(first_name, last_name, email, password_hash, about_me, picture_url) VALUES
('Elene', 'Enukidze', 'elene@gmail.com',  '123', 'CS student at freeuni', NULL),
('Giorgi', 'Giorgidze', 'giorgi125giorgi@gmail.com', '1234','math tutor', NULL),
('Ana', 'anashvili', 'anaanashvili7@gmail.com', '12345','Certified Tbilisi tour guide', NULL),
('Tim', 'Tom', 'TimaTom555@gmail.com', '123456','...', NULL),
('Lizi', 'Nanava', 'Lizi@gmail.com', '1234567','CS student', NULL),
('Davit', 'Palelashvili', 'davit@gmail.com', '12345678','Coding', NULL),
('Ato', 'Babilodze', 'ato@gmail.com', '123456789','savardzelimdivani', NULL),
('Saba', 'Makhniashvili', 'saba@gmail.com', '1234567890','Codeforces', NULL);



INSERT INTO services(provider_id, title, bio, picture_url, price) VALUES
(1, 'Basics of pottery', 'Learn with hands-on experience', NULL, 5),
(1, 'How to train a puppy?', '', NULL, 10),
(2, 'Learn Game Development', 'Join us as we learn C#', NULL, 15),
(5, 'Dog walker', 'Walking dogs', NULL, 20);

INSERT INTO slots(service_id, start_time, end_time) VALUES
                                                    (1, '2026-01-15 14:30:00', '2026-01-15 18:30:00'),
                                                    (4, '2026-01-15 15:30:00', '2026-01-15 16:30:00'),
                                                    (3, '2026-01-15 16:30:00', '2026-01-15 17:30:00'),
                                                    (4, '2026-01-15 17:30:00', '2026-01-15 18:30:00');

INSERT INTO bookings(taker_id, slot_id, status) VALUES
(1, 3, 'Pending'),
(4, 1, 'Pending'),
(3, 1, 'Pending'),
(4, 4, 'Pending'),
(6, 4, 'Pending'),
(7, 4, 'Pending'),
(7, 1, 'Pending');


