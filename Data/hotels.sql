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
 resID INT,
 FOREIGN KEY (resID) REFERENCES Reservations (resID),
 FOREIGN KEY (uID) REFERENCES Users (uID)
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

DROP PROCEDURE IF EXISTS archiveRes;
DELIMITER //
CREATE PROCEDURE archiveRes(IN cutOff DATE)
BEGIN
	INSERT INTO Archive(resID, roomID, uID, totalPrice, checkIn, checkOut, updatedAt)
    SELECT *
    FROM Reservations
    WHERE DATE(updatedAt) < cutOff;
	DELETE FROM Reservations
	WHERE DATE(updatedAt) < cutOff;

END //
DELIMITER ;

DROP TRIGGER IF EXISTS Conflict1;
DELIMITER |
CREATE TRIGGER Conflict1
BEFORE INSERT ON Reservations
FOR EACH ROW 
BEGIN
	IF EXISTS( SELECT * FROM Reservations WHERE new.checkIn <= checkOut AND new.checkOut >= checkIn AND new.roomID = roomID) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A reservation was made during selected date. Please try again';
	END IF;
END;|
DELIMITER ;


DROP TRIGGER IF EXISTS Conflict2;
DELIMITER |
CREATE TRIGGER Conflict2
BEFORE INSERT ON Reservations
FOR EACH ROW
BEGIN
	IF (New.checkin > New.checkout) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Invalid reservation time, check-out date should be after check-in date';
    END IF;
END;|
DELIMITER ;

LOAD DATA LOCAL INFILE 'D:/SJSU/CS157A/Champs/Data/users.txt' INTO TABLE Users;
LOAD DATA LOCAL INFILE 'D:/SJSU/CS157A/Champs/Data/rooms.txt' INTO TABLE Rooms;
LOAD DATA LOCAL INFILE 'D:/SJSU/CS157A/Champs/Data/amenities.txt' INTO TABLE Amenities;
LOAD DATA LOCAL INFILE 'D:/SJSU/CS157A/Champs/Data/ratings.txt' INTO TABLE Rating;
LOAD DATA LOCAL INFILE 'D:/SJSU/CS157A/Champs/Data/reservations.txt' INTO TABLE Reservations;