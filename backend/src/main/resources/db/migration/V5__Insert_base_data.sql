INSERT INTO users (first_name, last_name, email,
                   password_hash, about_me, picture_url,
                   is_enabled, verification_code) VALUES
                                                          ('Elene', 'Enukidze', 'elene@gmail.com',  '{bcrypt}$2a$10$T.LVw3y7H/qZM76BJuZ0G.SEPSNa9JSc.FerJH.KFbtgjEx7T5V56', 'CS student at freeuni', NULL, 1, NULL),
                                                          ('Giorgi', 'Giorgidze', 'giorgi125giorgi@gmail.com', '{bcrypt}$2a$10$MACVc9HU6CmCAQH0iOfryOc8ifcnA8NDRHfBpSBhIfWIAZgwzZBrm','math tutor', NULL, 1, NULL),
                                                          ('Ana', 'anashvili', 'anaanashvili7@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Certified Tbilisi tour guide', NULL, 1, NULL),
                                                          ('Tim', 'Tom', 'TimaTom555@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','...', NULL, 1, NULL),
                                                          ('Lizi', 'Nanava', 'Lizi@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','CS student', NULL, 1, NULL),
                                                          ('Davit', 'Palelashvili', 'davit@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Coding', NULL, 1, NULL),
                                                          ('Ato', 'Babilodze', 'ato@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','savardzelimdivani', NULL, 1, NULL),
                                                          ('Saba', 'Makhniashvili', 'saba@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Codeforces', NULL, 1, NULL);



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


