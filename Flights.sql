CREATE DATABASE flight_reservation;
USE flight_reservation;
CREATE TABLE IF NOT EXISTS flights (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_name VARCHAR(255),
    source VARCHAR(255),
    destination VARCHAR(255),
    date DATE,
    seats_available INT
);
CREATE TABLE IF NOT EXISTS tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(255),
    flight_name VARCHAR(255),
    source VARCHAR(255),
    destination VARCHAR(255),
    mobile_number VARCHAR(30),
    flight_id INT,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);
INSERT INTO flights(flight_name,source,destination,date,seats_available)VALUES("Indigo","Chennai","Guwahati",'2024-03-26',12);
INSERT INTO flights(flight_name,source,destination,date,seats_available)VALUES("Spicjet","NewDelhi","Mumbai",'2024-03-28',10);
INSERT INTO flights(flight_name,source,destination,date,seats_available)VALUES("AirIndia","Bengaluru","Hyderabad",'2024-03-30',8);
INSERT INTO flights(flight_name,source,destination,date,seats_available)VALUES("AlianceAir","Hyderabad","Chennai",'2024-04-02',6);
INSERT INTO flights(flight_name,source,destination,date,seats_available)VALUES("Vistara","Mumbai","Chennai",'2024-04-04',4);
SELECT*FROM flights;
SELECT*FROM tickets;
DROP DATABASE flight_reservation;
DROP TABLE flights;
DROP TABLE tickets;