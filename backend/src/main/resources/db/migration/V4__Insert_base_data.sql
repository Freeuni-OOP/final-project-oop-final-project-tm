INSERT INTO users(first_name, last_name, email, password_hash, about_me, picture_url) VALUES
('Elene', 'Enukidze', 'elene@gmail.com',  '{bcrypt}$2a$10$T.LVw3y7H/qZM76BJuZ0G.SEPSNa9JSc.FerJH.KFbtgjEx7T5V56', 'CS student at freeuni', NULL),
('Giorgi', 'Giorgidze', 'giorgi125giorgi@gmail.com', '{bcrypt}$2a$10$MACVc9HU6CmCAQH0iOfryOc8ifcnA8NDRHfBpSBhIfWIAZgwzZBrm','math tutor', NULL),
('Ana', 'anashvili', 'anaanashvili7@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Certified Tbilisi tour guide', NULL),
('Tim', 'Tom', 'TimaTom555@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','...', NULL),
('Lizi', 'Nanava', 'Lizi@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','CS student', NULL),
('Davit', 'Palelashvili', 'davit@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Coding', NULL),
('Ato', 'Babilodze', 'ato@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','savardzelimdivani', NULL),
('Saba', 'Makhniashvili', 'saba@gmail.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G','Codeforces', NULL);
-- the passwords are '123' '124' and rest are just '1' for simplicity


INSERT INTO services(provider_id, title, bio, picture_url) VALUES
(1, 'Basics of pottery', 'Learn with hands-on experience', NULL),
(1, 'How to train a puppy?', '', NULL),
(2, 'Learn Game Development', 'Join us as we learn C#', NULL),
(5, 'Dog walker', 'Walking dogs', NULL);


INSERT INTO bookings(taker_id, service_id, status) VALUES
(1, 3, 'Pending'),
(4, 1, 'Pending'),
(3, 1, 'Pending'),
(4, 4, 'Pending'),
(6, 4, 'Pending'),
(7, 4, 'Pending'),
(7, 1, 'Pending');