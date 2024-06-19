CREATE TABLE plan_types(
plan_type_id int NOT NULL,
plan_name VARCHAR(50),
PRIMARY KEY (plan_type_id)
);

CREATE TABLE hotel(
hotel_id VARCHAR(50) NOT NULL,
hotel_name VARCHAR(50) NOT NULL,
category_code  int NOT NULL,
hotel_address VARCHAR(100) NOT NULL,
hotel_checkin VARCHAR(20) NOT NULL,
hotel_checkout VARCHAR(20) NOT NULL,
PRIMARY KEY (hotel_id)
);

CREATE TABLE members(
member_id int NOT NULL AUTO_INCREMENT,
member_name VARCHAR(50) NOT NULL,
member_address VARCHAR(100) NOT NULL,
member_tel VARCHAR(20) NOT NULL,
member_email VARCHAR(20) NOT NULL,
member_birth VARCHAR(20) NOT NULL,
member_join VARCHAR(20) NOT NULL,
member_withdrawal VARCHAR(20),
member_password VARCHAR(50) NOT NULL,
PRIMARY KEY (member_id)
);

CREATE TABLE plans(
plan_id int NOT NULL,
hotel_id VARCHAR(50) NOT NULL,
plan_type_id int,
plan_price int,
plan_room_count int,
plan_dalete_date VARCHAR(15),
plan_description VARCHAR(100),
PRIMARY KEY (plan_id),
FOREIGN KEY(hotel_id)
REFERENCES hotel(hotel_id),
FOREIGN KEY(plan_type_id)
REFERENCES plan_types(plan_type_id)
);

CREATE TABLE reservations(
rsv_id int AUTO_INCREMENT,
member_id int NOT NULL,
plan_id int NOT NULL,
rsv_date VARCHAR(20),
rsv_checkin VARCHAR(20),
rsv_checkout VARCHAR(20),
rsv_room_count int,
PRIMARY KEY (rsv_id),
FOREIGN KEY(member_id)
REFERENCES members(member_id),
FOREIGN KEY(plan_id)
REFERENCES plans(plan_id)
);

CREATE TABLE reviews(
review_id int AUTO_INCREMENT,
member_id int,
hotel_id VARCHAR(50) NOT NULL,
rating int,
comment VARCHAR(255),
PRIMARY KEY (review_id),
FOREIGN KEY(member_id)
REFERENCES members(member_id),
FOREIGN KEY(hotel_id)
REFERENCES hotel(hotel_id)
);