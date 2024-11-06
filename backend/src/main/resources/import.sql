-- src/main/resources/import.sql

-- Insert data into Lodging table
INSERT INTO lodging (id, name, type, available_from, available_to, creation_date, update_date)
VALUES
    ('1', 'Hotel Sunshine', 'HOTEL', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW()),
    ('2', 'Hostel Happy', 'HOSTEL', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW()),
    ('3', 'Department Delight', 'DEPARTMENT', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW()),
-- Add more records up to 20
    ('20', 'Bed & Breakfast Cozy', 'BED_AND_BREAKFAST', '2023-01-01T00:00:00', '2023-12-31T23:59:59', NOW(), NOW());

-- Insert data into Photo table
INSERT INTO photo (id, url, lodging_id, creation_date, update_date)
VALUES
    ('1', 'http://example.com/photo1.jpg', '1', NOW(), NOW()),
    ('2', 'http://example.com/photo2.jpg', '2', NOW(), NOW()),
    ('3', 'http://example.com/photo3.jpg', '3', NOW(), NOW()),
-- Add more records up to 20
    ('20', 'http://example.com/photo20.jpg', '20', NOW(), NOW());

-- Insert data into Customer table
INSERT INTO customer (id, username, password, email, creation_date, update_date)
VALUES
    ('1', 'user1', 'password1', 'user1@example.com', NOW(), NOW()),
    ('2', 'user2', 'password2', 'user2@example.com', NOW(), NOW()),
    ('3', 'user3', 'password3', 'user3@example.com', NOW(), NOW()),
-- Add more records up to 20
    ('20', 'user20', 'password20', 'user20@example.com', NOW(), NOW());

-- Insert data into Reservation table
INSERT INTO reservation (id, start_date, end_date, customer_id, lodging_id, creation_date, update_date)
VALUES
    ('1', '2023-01-01T00:00:00', '2023-01-02T00:00:00', '1', '1', NOW(), NOW()),
    ('2', '2023-01-03T00:00:00', '2023-01-04T00:00:00', '2', '2', NOW(), NOW()),
    ('3', '2023-01-05T00:00:00', '2023-01-06T00:00:00', '3', '3', NOW(), NOW()),
-- Add more records up to 20
    ('20', '2023-01-39T00:00:00', '2023-01-40T00:00:00', '20', '20', NOW(), NOW());