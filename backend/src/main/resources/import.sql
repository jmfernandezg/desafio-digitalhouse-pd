-- Insert data into Lodging table
INSERT INTO lodging (id, name, type, available_from, available_to, creation_date, update_date) VALUES ('1', 'Hotel Sunshine', 'HOTEL', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW());
INSERT INTO lodging (id, name, type, available_from, available_to, creation_date, update_date) VALUES ('2', 'Hostel Happy', 'HOSTEL', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW());
INSERT INTO lodging (id, name, type, available_from, available_to, creation_date, update_date) VALUES ('3', 'Department Delight', 'DEPARTMENT', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW());
INSERT INTO lodging (id, name, type, available_from, available_to, creation_date, update_date) VALUES ('4', 'Bed & Breakfast Cozy', 'BED_AND_BREAKFAST', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW());

-- Insert data into Photo table
INSERT INTO photo (id, url, lodging_id, creation_date, update_date) VALUES ('1', 'http://localhost:8080/static/photo1.jpg', '1', NOW(), NOW());
INSERT INTO photo (id, url, lodging_id, creation_date, update_date) VALUES ('2', 'http://localhost:8080/static/photo2.jpg', '2', NOW(), NOW());
INSERT INTO photo (id, url, lodging_id, creation_date, update_date) VALUES ('3', 'http://localhost:8080/static/photo3.jpg', '3', NOW(), NOW());
INSERT INTO photo (id, url, lodging_id, creation_date, update_date) VALUES ('4', 'http://localhost:8080/static/photo4.jpg', '4', NOW(), NOW());


-- Insert data into Customer table
INSERT INTO customer (id, username, password, email, creation_date, update_date) VALUES ('1', 'user1', 'password1', 'user1@example.com', NOW(), NOW());

-- Insert data into Reservation table
INSERT INTO reservation (id, start_date, end_date, customer_id, lodging_id, creation_date, update_date) VALUES ('1', '2023-01-01T00:00:00', '2023-01-02T00:00:00', '1', '1', NOW(), NOW());