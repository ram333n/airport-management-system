INSERT INTO crew_members (pass_number, name, position, surname) VALUES ('PLT-123', 'Roman', 'PILOT', 'Prokopchuk');
INSERT INTO crew_members (pass_number, name, position, surname) VALUES ('PLT-431', 'Petro', 'PILOT', 'Donchuk-Dontsov');
INSERT INTO crew_members (pass_number, name, position, surname) VALUES ('OPR-244', 'Stepan', 'OPERATOR', 'Ilchuk');
INSERT INTO crew_members (pass_number, name, position, surname) VALUES ('NVG-355', 'Viktor', 'NAVIGATOR', 'Muzyka');

INSERT INTO flights (flight_number, departure_time, departure_from, arrival_time, destination)
VALUES ('MAU-111', '2023-05-23 23:53:00' , 'Kyiv', '2023-05-24 16:09:00', 'Krakow');

INSERT INTO flights (flight_number, departure_time, departure_from, arrival_time, destination)
VALUES ('MAU-955', '2023-05-24 06:41:00' , 'Frankurt', '2023-05-24 20:59:00', 'New York');

INSERT INTO flights (flight_number, departure_time, departure_from, arrival_time, destination)
VALUES ('TA-312', '2023-04-12 15:12:00' , 'Ankara', '2023-04-13 00:49:00', 'Berlin');

INSERT INTO flights_crew (pass_number, flight_number) VALUES ('PLT-123', 'MAU-111');
INSERT INTO flights_crew (pass_number, flight_number) VALUES ('OPR-244', 'MAU-111');
INSERT INTO flights_crew (pass_number, flight_number) VALUES ('NVG-355', 'MAU-111');

INSERT INTO flights_crew (pass_number, flight_number) VALUES ('PLT-431', 'MAU-955');
INSERT INTO flights_crew (pass_number, flight_number) VALUES ('OPR-244', 'MAU-955');

INSERT INTO flights_crew (pass_number, flight_number) VALUES ('PLT-123', 'TA-312');
INSERT INTO flights_crew (pass_number, flight_number) VALUES ('NVG-355', 'TA-312');

