DROP DATABASE IF EXISTS HOTEL;
CREATE DATABASE HOTEL;
USE HOTEL;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users
(uID INT AUTO_INCREMENT,
 fullName VARCHAR(50),
 email VARCHAR(50),
 phone VARCHAR(15),
 role VARCHAR(15),
 password VARCHAR(20),
 PRIMARY KEY (uID)
);
ALTER TABLE Users AUTO_INCREMENT = 101;

DROP TABLE IF EXISTS Rooms;
CREATE TABLE Rooms
(roomID INT AUTO_INCREMENT,
 roomType VARCHAR(10),
 roomNumber INT, 
 numOfBeds INT,
 capacity INT,
 price INT,
 PRIMARY KEY (roomID)
);
ALTER TABLE Rooms AUTO_INCREMENT = 1001;

DROP TABLE IF EXISTS Amenities;
CREATE TABLE Amenities
(roomID INT,
 internet BOOLEAN,
 roomService BOOLEAN,
 television BOOLEAN,
 FOREIGN KEY (roomID) REFERENCES Rooms(roomID)
);

DROP TABLE IF EXISTS Reservations;
CREATE TABLE Reservations
(resID INT AUTO_INCREMENT,
 roomID INT,
 uID INT,
 totalPrice INT,
 checkin DATE,
 checkout DATE,
 updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (resID),
 FOREIGN KEY (roomID) REFERENCES Rooms (roomID),
 FOREIGN KEY (uID) REFERENCES Users (uID) ON DELETE CASCADE
);
ALTER TABLE Reservations AUTO_INCREMENT = 2001;

DROP TABLE IF EXISTS Rating;
CREATE TABLE Rating
(score INT CHECK(score >= 1 AND score <= 5),
 review VARCHAR(150),
 uID INT,
 FOREIGN KEY(uID) REFERENCES Users(uID)
);

DROP TABLE IF EXISTS Archive;
CREATE TABLE Archive
(resID INT,
 roomID INT,
 uID INT,
 totalPrice INT,
 checkIn DATE,
 checkOut DATE,
 updatedAt DATE
);

LOAD DATA LOCAL INFILE '/Users/oobnala/Desktop/cs157A/Champs/users.txt' INTO TABLE Users;
LOAD DATA LOCAL INFILE '/Users/oobnala/Desktop/cs157A/Champs/rooms.txt' INTO TABLE Rooms;
LOAD DATA LOCAL INFILE '/Users/oobnala/Desktop/cs157A/Champs/amenities.txt' INTO TABLE Amenities;