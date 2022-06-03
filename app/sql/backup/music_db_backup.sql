CREATE DATABASE IF NOT EXISTS music_db;

CREATE TABLE IF NOT EXISTS Artists(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(255) UNIQUE NOT NULL
);
INSERT INTO Artists(NAME) VALUES('Davido');
INSERT INTO Artists(NAME) VALUES('Diamond Platnumz');
INSERT INTO Artists(NAME) VALUES('Wizkid');
INSERT INTO Artists(NAME) VALUES('Fireboy DML');
INSERT INTO Artists(NAME) VALUES('Bisa Kdei');
INSERT INTO Artists(NAME) VALUES('Olamide');
INSERT INTO Artists(NAME) VALUES('Sauti Sol');
INSERT INTO Artists(NAME) VALUES('Emmerson');
INSERT INTO Artists(NAME) VALUES('Drizilik');
INSERT INTO Artists(NAME) VALUES('Adekunle Gold');
INSERT INTO Artists(NAME) VALUES('Burna Boy');
INSERT INTO Artists(NAME) VALUES('Fela Kuti');
INSERT INTO Artists(NAME) VALUES('Shine P');
INSERT INTO Artists(NAME) VALUES('Magic System');
INSERT INTO Artists(NAME) VALUES('Kzeebigname');

CREATE TABLE IF NOT EXISTS Albums(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    ArtistID INT NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    FOREIGN KEY (ArtistID) REFERENCES Artists(ID)
);
INSERT INTO Albums(ArtistID, NAME) VALUES(1, 'Single');
INSERT INTO Albums(ArtistID, NAME) VALUES(1, 'A Better Time');
INSERT INTO Albums(ArtistID, NAME) VALUES(2, 'Single');
INSERT INTO Albums(ArtistID, NAME) VALUES(3, 'Ayo');
INSERT INTO Albums(ArtistID, NAME) VALUES(3, 'Single');
INSERT INTO Albums(ArtistID, NAME) VALUES(3, 'Made in Lagos');
INSERT INTO Albums(ArtistID, NAME) VALUES(4, 'Laughter, Tears & Goosebumps');
INSERT INTO Albums(ArtistID, NAME) VALUES(4, 'APOLLO');
INSERT INTO Albums(ArtistID, NAME) VALUES(5, 'Breakthrough');
INSERT INTO Albums(ArtistID, NAME) VALUES(6, 'Eyan Maywheather');
INSERT INTO Albums(ArtistID, NAME) VALUES(6, 'YBNL');
INSERT INTO Albums(ArtistID, NAME) VALUES(6, 'UV Scuti');
INSERT INTO Albums(ArtistID, NAME) VALUES(6, 'Carpe Diem');
INSERT INTO Albums(ArtistID, NAME) VALUES(7, 'Afrikan Sauce');
INSERT INTO Albums(ArtistID, NAME) VALUES(7, 'Single');
INSERT INTO Albums(ArtistID, NAME) VALUES(8, '2 Fut Arata');
INSERT INTO Albums(ArtistID, NAME) VALUES(8, 'Borbor Bele');
INSERT INTO Albums(ArtistID, NAME) VALUES(8, 'Survivor');
INSERT INTO Albums(ArtistID, NAME) VALUES(8, '9 Lives');
INSERT INTO Albums(ArtistID, NAME) VALUES(9, 'Shukubly');
INSERT INTO Albums(ArtistID, NAME) VALUES(10, 'Gold');
INSERT INTO Albums(ArtistID, NAME) VALUES(10, 'About 30');
INSERT INTO Albums(ArtistID, NAME) VALUES(11, 'The Lion King: The Gift');
INSERT INTO Albums(ArtistID, NAME) VALUES(11, 'African Giant');
INSERT INTO Albums(ArtistID, NAME) VALUES(12, 'Best of the Black President');
INSERT INTO Albums(ArtistID, NAME) VALUES(13, 'Still Shine P');
INSERT INTO Albums(ArtistID, NAME) VALUES(13, 'Single');
INSERT INTO Albums(ArtistID, NAME) VALUES(14, 'Premier Gaou');
INSERT INTO Albums(ArtistID, NAME) VALUES(15, 'Best of Kzeebigname, Vol. 1');

CREATE TABLE IF NOT EXISTS Songs(
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Track INT NOT NULL,
    AlbumID INT NOT NULL,
    FOREIGN KEY (AlbumID) REFERENCES Albums(ID)
);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('If', 1, 1);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Fall', 2, 1);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('FEM', 3, 1);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('La la', 12, 2);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Kanyanga', 1, 3);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Baba Lao', 2, 3);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Jeje', 3, 3);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Marry You', 4, 3);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Ojuelegba', 3, 4);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Joro', 1, 5);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Reckless', 1, 6);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('No Stress', 8, 6);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Anonti', 15, 6);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Vibration', 2, 7);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Scatter', 3, 7);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('What If I Say', 12, 7);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Champion', 1, 8);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Spell', 2, 8);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Remember Me', 17, 8);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Mansa', 1, 9);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Matters Arising', 13, 10);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Bobo', 18, 10);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('First of All', 19, 11);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Rock', 3, 12);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Infinity', 3, 13);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Plenty', 12, 13);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Short N Sweet', 12, 14);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Extravaganza', 1, 15);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Suzanna', 2, 15);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Borbor Pain', 3, 16);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Tutu Party', 3, 17);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Uman Lapi', 11, 17);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Pwel Am', 11, 18);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Secure', 11, 19);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Posin', 8, 20);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Yu Fil Se Na Fulish', 2, 20);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Friend Zone', 6, 21);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Sade', 16, 21);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Mr. Foolish', 3, 22);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('JA ARA E', 8, 23);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('African Giant', 1, 24);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Anyboy', 2, 24);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('On The Low', 16, 24);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Zombie', 5, 25);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Water No Get Enemy', 4, 25);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Lady', 1, 25);

INSERT INTO Songs(Title, Track, AlbumID) VALUES('Drinking Drinks', 3, 26);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Mind your Baynay', 1, 27);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Premier Gaou', 1, 28);
INSERT INTO Songs(Title, Track, AlbumID) VALUES('Spoil You With Love', 5, 29);

CREATE OR REPLACE VIEW ArtistList AS
SELECT art.NAME as `Artist`, s.Title as `Song`, s.Track, alb.NAME AS `Album`
FROM Artists art
JOIN Albums alb ON alb.ArtistID = art.ID
JOIN Songs s ON s.AlbumID = alb.ID
WHERE art.NAME IS NOT NULL;

COMMIT;