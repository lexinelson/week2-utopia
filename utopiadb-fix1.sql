use utopia;

-- some of the booking was unaccounted
INSERT INTO flight_bookings
VALUES (1, 6), (2, 7), (3, 9);

-- these should be reflective of what's recorded in flight_bookings
UPDATE flight
SET reserved_seats = 1
WHERE id > 4;

UPDATE flight
SET reserved_seats = 2
WHERE id < 4;


-- CREATE A SEAT CLASSIFICATION ENTITY

CREATE TABLE IF NOT EXISTS seat_class (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  adjuster INT UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX id_UNIQUE (id ASC),
  UNIQUE INDEX name_UNIQUE (name ASC))
ENGINE = InnoDB;

INSERT INTO utopia.seat_class (name, adjuster)
VALUES ("First Class", 3), ("Business Class", 2), ("Economy Class", 1);


-- MANY TO MANY SEAT SECTIONS RELATION (flight & seat_class)

CREATE TABLE IF NOT EXISTS flight_seats (
  flight_id INT UNSIGNED NOT NULL,
  seat_id INT UNSIGNED NOT NULL,
  capacity INT UNSIGNED NOT NULL,
  INDEX fk_flight_seats_flight (flight_id ASC),
  INDEX fk_flight_seats_seat (seat_id ASC),
  PRIMARY KEY (flight_id, seat_id),
  CONSTRAINT fk_flight_seats_flight
    FOREIGN KEY (flight_id)
    REFERENCES flight (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_flight_seats_seat
    FOREIGN KEY (seat_id)
    REFERENCES seat_class (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO flight_seats
VALUES (1, 1, 10), (1, 2, 40), (1, 3, 50), (2, 1, 5), (2, 2, 15), (2, 3, 30),
	 (3, 1, 20), (3, 2, 30), (3, 3, 55), (4, 3, 10), (5, 1, 10), (5, 2, 35), (5, 3, 45),
     (6, 2, 10), (6, 3, 30), (7, 1, 10), (7, 2, 25), (7, 3, 60), (8, 1, 10), (8, 2, 25), (8, 3, 60),
     (9, 1, 5), (9, 2, 20), (9, 3, 50), (10, 1, 10), (10, 2, 20), (10, 3, 60);



-- EACH Booking is for a seat type - all flights on the booking will be of that type
CREATE TABLE IF NOT EXISTS booking_seats (
  booking_id INT UNSIGNED NOT NULL,
  seat_id INT UNSIGNED NOT NULL,
  INDEX fk_booking_seats_booking (booking_id ASC),
  INDEX fk_booking_seats_seat (seat_id ASC),
  PRIMARY KEY (booking_id, seat_id),
  CONSTRAINT fk_booking_seats_booking
    FOREIGN KEY (booking_id)
    REFERENCES booking (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_booking_seats_seat
    FOREIGN KEY (seat_id)
    REFERENCES seat_class (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO booking_seats
VALUES (1, 2), (2, 3), (3, 1), (4, 3), (5, 2), (6, 1), (7, 2), (8, 3), (9, 2), (10, 3);