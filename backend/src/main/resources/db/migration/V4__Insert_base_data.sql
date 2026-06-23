INSERT INTO users(first_name, last_name, email, password_hash, about_me, picture_url) VALUES
('Elene', 'Enukidze', 'elene@gmail.com',  '123', 'CS student at freeuni', NULL),
('Giorgi', 'Giorgidze', 'giorgi125giorgi@gmail.com', '1234','math tutor', NULL),
('Ana', 'anashvili', 'anaanashvili7@gmail.com', '12345','Certified Tbilisi tour guide', NULL),
('Tim', 'Tom', 'TimaTom555@gmail.com', '123456','...', NULL),
('Lizi', 'Nanava', 'Lizi@gmail.com', '1234567','CS student', NULL),
('Davit', 'Palelashvili', 'davit@gmail.com', '12345678','Coding', NULL),
('Ato', 'Babilodze', 'ato@gmail.com', '123456789','savardzelimdivani', NULL),
('Saba', 'Makhniashvili', 'saba@gmail.com', '1234567890','Codeforces', NULL);



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