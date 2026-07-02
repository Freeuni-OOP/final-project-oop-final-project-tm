INSERT INTO users(first_name, last_name, email, about_me, picture_url) VALUES
('Elene', 'Enukidze', 'elene@gmail.com', 'CS student at freeuni', NULL),
('Giorgi', 'Giorgidze', 'giorgi125giorgi@gmail.com', 'math tutor', NULL),
('Ana', 'anashvili', 'anaanashvili7@gmail.com', 'Certified Tbilisi tour guide', NULL),
('Tim', 'Tom', 'TimaTom555@gmail.com', '...', NULL),
('Lizi', 'Nanava', 'Lizi@gmail.com', 'CS student', NULL),
('Davit', 'Palelashvili', 'davit@gmail.com', 'Coding', NULL),
('Ato', 'Babilodze', 'ato@gmail.com', 'savardzelimdivani', NULL),
('Saba', 'Makhniashvili', 'saba@gmail.com', 'Codeforces', NULL);



INSERT INTO services(provider_id, title, bio, picture_url, category, price) VALUES
                                                                                (1, 'Basics of pottery', 'Learn with hands-on experience', NULL, 'Math & Physics', 200.00),
                                                                                (1, 'How to train a puppy?', 'lalala', NULL, 'Biology & Chemistry', 100.00),
                                                                                (2, 'Learn Game Development', 'Join us as we learn C#', NULL, 'IT & Programming', 50.00),
                                                                                (5, 'Dog walker', 'Walking dogs', NULL, 'Music & Arts', 500.00);

INSERT INTO bookings(taker_id, service_id) VALUES
(1, 3),
(4, 1),
(3, 1),
(4, 4),
(6, 4),
(7, 4),
(7, 1);