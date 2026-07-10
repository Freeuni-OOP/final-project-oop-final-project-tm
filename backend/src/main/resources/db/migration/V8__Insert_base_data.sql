INSERT INTO users (user_id, first_name, last_name, email, password_hash, is_enabled) VALUES
                                                                                         (1, 'Amara', 'Okonkwo', 'amara.o@example.com', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (2, 'Hiroshi', 'Tanaka', 'h.tanaka@example.jp', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (3, 'Elena', 'Petrova', 'elena.pet@example.bg', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (4, 'Mateo', 'Fernández', 'mateo.f@example.ar', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (5, 'Sanaa', 'Al-Mansour', 'sanaa.am@example.ae', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (6, 'Lars', 'Jensen', 'lars.j@example.dk', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (7, 'Fatoumata', 'Diop', 'f.diop@example.sn', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1),
                                                                                         (8, 'Alessandro', 'Rossi', 'ale.rossi@example.it', '{bcrypt}$2a$10$8QvDfni0jK7iujf14YZkheizru3nZdOAnToEBdQiVh9zMOTkKhZ6G', 1);

INSERT INTO services (service_id, provider_id, title, bio, image_path, price, category) VALUES
                                                                                            (1, 1, 'Introduction to Sustainable Architecture', 'In this comprehensive workshop, we explore the integration of eco-friendly materials into modern home design...', 'https://fastly.picsum.photos/id/288/200/200.jpg', 75.00, 'Architecture'),
                                                                                            (2, 1, 'Lagos Urban Planning Tour', 'Join me for an immersive walking tour of Lagos...', 'https://fastly.picsum.photos/id/434/200/200.jpg', 40.00, 'Travel'),
                                                                                            (3, 2, 'Mastering C# for Unity Development', 'From absolute beginner to building your first 2D platformer...', 'https://fastly.picsum.photos/id/0/200/200.jpg', 120.00, 'IT & Programming'),
                                                                                            (4, 2, 'Vintage Watch Restoration Basics', 'A delicate, meditative dive into the world of horology...', 'https://images.squarespace-cdn.com/content/v1/...', 90.00, 'Arts'),
                                                                                            (5, 3, 'Alpine Flora Identification Field Trip', 'Spend a day with me in the mountains learning how to identify endemic plant species...', 'https://fastly.picsum.photos/id/519/200/200.jpg', 60.00, 'Education'),
                                                                                            (6, 3, 'Nature Photography Masterclass', 'Capture the wilderness like a pro...', 'https://upload.wikimedia.org/wikipedia/commons/...', 55.00, 'Arts'),
                                                                                            (7, 4, 'The Science of Espresso Extraction', 'Beyond the button press...', 'https://upload.wikimedia.org/wikipedia/commons/...', 45.00, 'Education'),
                                                                                            (8, 4, 'Jazz Bass Fundamentals', 'A beginner-friendly guide to walking bass lines and jazz improvisation...', 'https://upload.wikimedia.org/wikipedia/commons/...', 35.00, 'Music'),
                                                                                            (9, 5, 'Tech Startup Branding Strategy', 'How to define your voice in a crowded market...', 'https://upload.wikimedia.org/wikipedia/commons/...', 150.00, 'Business'),
                                                                                            (10, 5, 'Professional Social Media Audit', 'Struggling to gain traction?...', 'https://upload.wikimedia.org/wikipedia/commons/...', 85.00, 'Business'),
                                                                                            (11, 6, 'Danish Modern Furniture Workshop', 'Learn to build a simple, elegant side table...', 'https://upload.wikimedia.org/wikipedia/commons/...', 200.00, 'Arts'),
                                                                                            (12, 6, 'Introduction to Nordic Woodworking', 'Learn the basics of hand-tool woodworking...', 'https://upload.wikimedia.org/wikipedia/commons/...', 110.00, 'Arts'),
                                                                                            (13, 7, 'Introduction to Civil Rights Advocacy', 'A foundational course on understanding your rights...', 'https://upload.wikimedia.org/wikipedia/commons/...', 65.00, 'Education'),
                                                                                            (14, 7, 'Traditional Weaving Workshop', 'A workshop on Senegalese weaving techniques...', 'https://upload.wikimedia.org/wikipedia/commons/...', 50.00, 'Arts'),
                                                                                            (15, 8, 'Authentic Sourdough Baking', 'Forget store-bought loaves...', 'https://upload.wikimedia.org/wikipedia/commons/...', 40.00, 'Education'),
                                                                                            (16, 8, 'Italian Home Cooking Secrets', 'Simple, high-quality ingredients made right...', 'https://upload.wikimedia.org/wikipedia/commons/...', 55.00, 'Education'),
                                                                                            (17, 1, 'Pottery Basics', 'Hands-on pottery workshop', NULL, 200.00, 'Arts'),
                                                                                            (18, 2, 'Calculus Tutoring', 'Advanced math help', NULL, 200.00, 'Math & Physics'),
                                                                                            (19, 3, 'Tbilisi City Tour', 'Customized local tours', NULL, 200.00, 'Travel'),
                                                                                            (20, 4, 'Programming Basics', 'Intro to coding', NULL, 200.00, 'IT & Programming'),
                                                                                        (21, 5, 'Painting Class', 'Learn to paint landscapes', NULL, 200.00, 'Music & Arts');















-- ========================================================
-- 1. სლოტების დამატება (SLOTS) სხვადასხვა სერვისებისთვის
-- ========================================================

-- Amara (provider_id = 1) -> სერვისები 1, 2, 17
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (1, 1, '2026-07-15 10:00:00', '2026-07-15 11:00:00'),
                                                                  (2, 1, '2026-07-15 11:00:00', '2026-07-15 12:00:00'),
                                                                  (3, 2, '2026-07-16 15:00:00', '2026-07-16 17:00:00');

-- Hiroshi (provider_id = 2) -> სერვისები 3, 4, 18
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (4, 3, '2026-07-15 09:00:00', '2026-07-15 11:00:00'),
                                                                  (5, 3, '2026-07-15 14:00:00', '2026-07-15 16:00:00'),
                                                                  (6, 4, '2026-07-17 11:00:00', '2026-07-17 13:00:00');

-- Elena (provider_id = 3) -> სერვისები 5, 6, 19
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (7, 5, '2026-07-18 10:00:00', '2026-07-18 14:00:00'),
                                                                  (8, 6, '2026-07-19 16:00:00', '2026-07-19 18:00:00'),
                                                                  (9, 19, '2026-07-20 12:00:00', '2026-07-20 14:00:00');

-- Mateo (provider_id = 4) -> სერვისები 7, 8, 20
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (10, 7, '2026-07-15 08:00:00', '2026-07-15 09:30:00'),
                                                                  (11, 8, '2026-07-16 18:00:00', '2026-07-16 19:30:00');

-- Sanaa (provider_id = 5) -> სერვისები 9, 10, 21
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (12, 9, '2026-07-20 10:00:00', '2026-07-20 12:00:00'),
                                                                  (13, 10, '2026-07-21 11:00:00', '2026-07-21 12:00:00');

-- Lars (provider_id = 6) -> სერვისები 11, 12
INSERT INTO slots (slot_id, service_id, start_time, end_time) VALUES
                                                                  (14, 11, '2026-07-22 10:00:00', '2026-07-22 13:00:00'),
                                                                  (15, 12, '2026-07-23 15:00:00', '2026-07-23 18:00:00');


-- ========================================================
-- 2. ჯავშნების დამატება (BOOKINGS)
-- ========================================================
-- taker_id არის ის იუზერი, ვინც ყიდულობს/ჯავშნის სერვისს

INSERT INTO bookings (taker_id, slot_id, status) VALUES
-- რეზერვაციები Amara-ს სლოტებზე (slot 1, 2, 3)
(2, 1, 'ACTIVE'),     -- Hiroshi-მ დაჯავშნა Amara-ს სერვისი 1
(3, 2, 'COMPLETED'),  -- Elena-მ დაასრულა Amara-ს სერვისი 1
(4, 3, 'ACTIVE'),     -- Mateo-მ დაჯავშნა Amara-ს სერვისი 2

-- რეზერვაციები Hiroshi-ს სლოტებზე (slot 4, 5, 6)
(1, 4, 'ACTIVE'),     -- Amara-მ დაჯავშნა Hiroshi-ს სერვისი 3
(5, 5, 'ACTIVE'),     -- Sanaa-მ დაჯავშნა Hiroshi-ს სერვისი 3
(6, 6, 'COMPLETED'),  -- Lars-მა დაასრულა Hiroshi-ს სერვისი 4

-- რეზერვაციები Elena-ს სლოტებზე (slot 7, 8, 9)
(7, 7, 'ACTIVE'),     -- Fatoumata-მ დაჯავშნა Elena-ს სერვისი 5
(8, 8, 'ACTIVE'),     -- Alessandro-მ დაჯავშნა Elena-ს სერვისი 6
(1, 9, 'PENDING'),    -- Amara-მ დაჯავშნა Elena-ს სერვისი 19 (თბილისის ტური)

-- რეზერვაციები Mateo-ს სლოტებზე (slot 10, 11)
(2, 10, 'ACTIVE'),    -- Hiroshi-მ დაჯავშნა Mateo-ს სერვისი 7
(3, 11, 'COMPLETED'), -- Elena-მ დაასრულა Mateo-ს სერვისი 8

-- რეზერვაციები Sanaa-ს სლოტებზე (slot 12, 13)
(4, 12, 'ACTIVE'),    -- Mateo-მ დაჯავშნა Sanaa-ს სერვისი 9
(6, 13, 'ACTIVE');    -- Lars-მა დაჯავშნა Sanaa-ს სერვისი 10