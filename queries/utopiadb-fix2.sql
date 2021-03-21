use utopia;

CREATE TABLE IF NOT EXISTS route_duration (
	route_id INT UNSIGNED NOT NULL,
    hours INT UNSIGNED NOT NULL,
PRIMARY KEY (route_id),
CONSTRAINT fk_route_duration_route
	FOREIGN KEY (route_id)
    REFERENCES route (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

INSERT INTO route_duration
VALUES (1, 1), (2, 3), (3, 6), (4, 2), (5, 4), (6, 2),
	 (7, 2), (8, 4), (9, 3), (10, 7), (11, 6), (12, 5);