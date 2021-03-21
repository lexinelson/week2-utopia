USE utopia;

INSERT INTO airplane_type (max_capacity)
VALUES (125), (115), (105), (95), (85), 
	(75), (70), (45), (25), (12);
    
INSERT INTO airplane (type_id)
VALUES (1), (1), (10), (6), (3), (4), (4),
    (2), (7), (7), (8), (9), (2), (6);
    
INSERT INTO airport
VALUES ('SAC', 'Sacramento'), ('LAX', 'Los Angeles'), ('DMV', 'Washington DC'), ('HST', 'Houston'), ('LVS', 'Las Vegas'),
    ('MSP', 'Minneapolis'), ('STL', 'Seattle'), ('NYC', 'New York City'), ('SFC', 'San Francisco'), ('CHC', 'Chicago'),
    ('SLC', 'Salt Lake City'), ('DTR', 'Detroit'), ('ATL', 'Atlanta'), ('MIA', 'Miami'), ('BST', 'Boston');
    
INSERT INTO route (origin_id, destination_id)
VALUES ('SAC', 'LAX'), ('NYC', 'DMV'), ('HST', 'BST'), ('LVS', 'HST'), ('MSP', 'DMV'), ('DMV', 'MIA'),
    ('SLC', 'SFC'), ('DTR', 'ATL'), ('BST', 'MIA'), ('LAX', 'CHC'), ('CHC', 'STL'), ('DMV', 'HST');
    
INSERT INTO flight
VALUES (001, 3, 5, '2021-04-05 10:35:00', 30, 204.65), (002, 7, 10, '2021-03-25 14:45:00', 15, 250.99), 
	(003, 6, 1, '2021-03-22 14:45:00', 70, 475.85), (004, 1, 3, '2021-03-28 05:15:00', 6, 113.54), 
    (005, 10, 6, '2021-03-30 9:42:00', 73, 372.99), (006, 2, 11, '2021-03-28 07:45:00',38, 560.75),
    (007, 4, 8, '2021-04-01 18:30:00', 86, 475.85), (008, 11, 2, '2021-03-20 10:10:00', 101, 600.90),
    (009, 5, 4, '2021-04-03 14:33:00', 55, 368.96), (010, 8, 13, '2021-03-26 15:40:00', 63, 400.70);
    
INSERT INTO booking (confirmation_code)
VALUES ('ABCDEF12345'), ('12345ABCDEF'), ('ABCDEF67890'), ('67890ABCDEF'), ('ABC12345DEF'),
	('ZYXWV123456'), ('123456ZYXWV'), ('ZYXWV567890'), ('567890ZYXWV'), ('123ZYXWV456');
    
INSERT INTO flight_bookings
VALUES (1, 5), (2, 4), (3, 8), (4, 2), (5, 1), (6, 10), (7, 9), (8, 3), (9, 1), (10, 5);
    
INSERT INTO booking_payment (booking_id, stripe_id)
VALUES (1, 'SIUADSFLKNSGKL'), (2, '28URE9QR8YD9DJ'), (3, 'SFHS934U8JDMKF'), (4, 'IJNDF99383497D'),
	(5, 'SOFUUEJNMI2380R'), (6, 'ER9I34H79DSKL'), (7, 'DFIL8H3IH89AP9'),  (8, '249UPE8HFR7E8A3'),
    (9, 'WR89JQRIL7DS93K'), (10, '1209RJID94HDU');

INSERT INTO passenger (booking_id, given_name, family_name, dob, gender, address)
VALUES (1, 'Alex', 'Morris', '1990-08-20', 'Female', '123 Somewhere St.'), (2, 'Luke', 'Mason', '1983-01-01', 'Male', '987 Avenew Ave.'),
	(3, 'Nick', 'Jonah', '1994-10-21', 'Male', '456 Realistic Blvd.'), (4, 'Jasmine', 'Maria', '1987-09-26', 'Female', '234 Main St.'),
    (5, 'Ashley', 'Paul', '1992-03-21', 'Female', '231 Smuggler Cove'), (6, 'Rori', 'Sanchez', '1997-02-13', 'Nonbinary', '1001 Golden Ave.'),
    (7, 'Dave', 'Shoemaker', '1974-11-17', 'Male', '435 Hoover Blvd.'), (8, 'Micheal', 'Bloomberg', '1963-08-12', 'Male', '101 Madison Ave.'),
    (9, 'Drake', 'Malo', '1981-12-03', 'Male', '905 Sunchine St.'), (10, 'Holly', 'Jackson', '1979-06-07', 'Female', '876 Home Blvd.');
    
INSERT INTO booking_guest
VALUES (1, 'am0820@email.com', '987-654-3210'), (2, 'lm0101@email.com', '123-456-7890'), (3, 'nj1021@email.com', '555-555-5555'),
	(4, 'jm0926@email.com', '234-567-8910'), (5, 'ap0321@email.com', '000-000-0000'), (6, 'rs0213@email.com', '444-444-4444'),
    (7, 'ds1117@email.com', '098-765-4321'), (8, 'mb0812@email.com', '012-345-6789'), (9, 'dm1203@email.com', '777-777-7777'),
    (10, 'hj0607@email.com', '010-987-6543');
    
INSERT INTO user_role (name)
VALUES ('administrator'), ('agent'), ('traveler');

INSERT INTO user (role_id, given_name, family_name, username, email, password, phone)
VALUES (1, 'Lexi', 'Nelson', 'admin', 'my_email@mail.com', '********', '111-111-1111'),
	(2, 'Richard', 'Deshes', 'emp-rick', 'rickemp@email.com', 'password123', '123-321-1234'),
    (2, 'Amy', 'Paulise', 'emp-amy', 'amyemp@email.com', 'Rhuew12304ms9', '333-333-3333'),
    (2, 'Kaitlyn', 'Rice', 'emp-kate', 'kateemp@email.com', 'letTherebeFood', '101-001-1111'),
    (3, 'Jasmine', 'Maria', 'jazzyflyer', 'jm0926@email.com', 'WhyIneedOne?!47', '234-567-8910'),
    (3, 'Dave', 'Shoemaker', 'golfdave', 'ds1117@email.com', 'dave11171974', '098-765-432'),
    (3, 'Holly', 'Jackson', 'happyholly', 'hj0607@email.com', 'beautifulmanchester', '010-987-6543');
    
INSERT INTO booking_user
VALUES (4, 5), (7, 6), (9, 6), (10, 7);
    
INSERT INTO booking_agent
VALUES (1, 2), (2, 4), (3, 2), (5, 3), (6, 3), (8, 4);