package groovyfx.music.db.config

import groovy.util.logging.Slf4j

import groovy.sql.*
import org.hsqldb.jdbc.JDBCDataSource

import java.sql.SQLException

@Slf4j
enum Datasource {
    INSTANCE;

    def CONNECTION_STRING = "jdbc:hsqldb:mem:music_db"
    def user = 'sa'
    def password = ''
//    def driver = 'org.hsqldb.jdbcDriver'

    JDBCDataSource dataSource
    Sql sql

//    private Connection connection = null

    /**
     * Connect to the Datasource.
     */
    boolean init() throws Exception {
        try {
            dataSource = new JDBCDataSource(database: CONNECTION_STRING, user: user, password: password)
            sql = new Sql(dataSource)

            if (sql != null) {
                log.debug("Successful DB connection.")
            } else {
                throw new IllegalAccessException("DB Connection failed!")
            }
            sql.execute("CREATE TABLE IF NOT EXISTS Artists(\n" +
                    "    ID INT IDENTITY PRIMARY KEY,\n" +
                    "    Name VARCHAR(255) UNIQUE NOT NULL\n" +
                    ");")
            sql.execute("CREATE TABLE IF NOT EXISTS Albums(\n" +
                    "    ID INT IDENTITY PRIMARY KEY,\n" +
                    "    ArtistID INT NOT NULL,\n" +
                    "    NAME VARCHAR(255) NOT NULL,\n" +
                    "    FOREIGN KEY (ArtistID) REFERENCES Artists(ID)\n" +
                    ");")
            sql.execute("CREATE TABLE IF NOT EXISTS Songs(\n" +
                    "    ID INT IDENTITY PRIMARY KEY,\n" +
                    "    Title VARCHAR(255) NOT NULL,\n" +
                    "    Track INT NOT NULL,\n" +
                    "    AlbumID INT NOT NULL,\n" +
                    "    FOREIGN KEY (AlbumID) REFERENCES Albums(ID)\n" +
                    ");")

            def insertArtist = "INSERT INTO Artists(NAME) VALUES(?);"
            [
                    'Davido',
                    'Diamond Platnumz',
                    'Wizkid',
                    'Fireboy DML',
                    'Bisa Kdei',
                    'Olamide',
                    'Sauti Sol',
                    'Emmerson',
                    'Drizilik',
                    'Adekunle Gold',
                    'Burna Boy',
                    'Fela Kuti',
                    'Shine P',
                    'Magic System',
                    'Kzeebigname'
            ].each {name -> sql.execute(insertArtist, name) }

            def insertAlbum = "INSERT INTO Albums(ArtistID, NAME) VALUES(?, ?);"
            sql.withBatch(insertAlbum, {
                it.addBatch(0, 'Single')
                it.addBatch(0, 'A Better Time')
                it.addBatch(1, 'Single')
                it.addBatch(2, 'Ayo')
                it.addBatch(2, 'Single')
                it.addBatch(2, 'Made in Lagos')
                it.addBatch(3, 'Laughter, Tears & Goosebumps')
                it.addBatch(3, 'APOLLO')
                it.addBatch(4, 'Breakthrough')
                it.addBatch(5, 'Eyan Maywheather')
                it.addBatch(5, 'YBNL')
                it.addBatch(5, 'UV Scuti')
                it.addBatch(5, 'Carpe Diem')
                it.addBatch(6, 'Afrikan Sauce')
                it.addBatch(6, 'Single')
                it.addBatch(7, '2 Fut Arata')
                it.addBatch(7, 'Borbor Bele')
                it.addBatch(7, 'Survivor')
                it.addBatch(7, '9 Lives')
                it.addBatch(8, 'Shukubly')
                it.addBatch(9, 'Gold')
                it.addBatch(9, 'About 30')
                it.addBatch(10, 'The Lion King: The Gift')
                it.addBatch(10, 'African Giant')
                it.addBatch(11, 'Best of the Black President')
                it.addBatch(12, 'Still Shine P')
                it.addBatch(12, 'Single')
                it.addBatch(13, 'Premier Gaou')
                it.addBatch(14, 'Best of Kzeebigname, Vol. 1')
            })

            def insertSong = "INSERT INTO Songs(Title, Track, AlbumID) VALUES(?, ?, ?);"
            sql.withBatch(insertSong, {
                it.addBatch('If', 1, 0);
                it.addBatch('Fall', 2, 0);
                it.addBatch('FEM', 3, 0);
                it.addBatch('La la', 12, 1);

                it.addBatch('Kanyanga', 1, 2);
                it.addBatch('Baba Lao', 2, 2);
                it.addBatch('Jeje', 3, 2);
                it.addBatch('Marry You', 4, 2);

                it.addBatch('Ojuelegba', 3, 3);
                it.addBatch('Joro', 1, 4);
                it.addBatch('Reckless', 1, 5);
                it.addBatch('No Stress', 8, 5);
                it.addBatch('Anonti', 15, 5);

                it.addBatch('Vibration', 2, 6);
                it.addBatch('Scatter', 3, 6);
                it.addBatch('What If I Say', 12, 6);

                it.addBatch('Champion', 1, 7);
                it.addBatch('Spell', 2, 7);
                it.addBatch('Remember Me', 17, 7);

                it.addBatch('Mansa', 1, 8);

                it.addBatch('Matters Arising', 13, 9);
                it.addBatch('Bobo', 18, 9);
                it.addBatch('First of All', 19, 10);
                it.addBatch('Rock', 3, 11);
                it.addBatch('Infinity', 3, 12);
                it.addBatch('Plenty', 12, 12);

                it.addBatch('Short N Sweet', 12, 13);
                it.addBatch('Extravaganza', 1, 14);
                it.addBatch('Suzanna', 2, 14);

                it.addBatch('Borbor Pain', 3, 15);
                it.addBatch('Tutu Party', 3, 16);
                it.addBatch('Uman Lapi', 11, 16);
                it.addBatch('Pwel Am', 11, 17);
                it.addBatch('Secure', 11, 18);

                it.addBatch('Posin', 8, 19);
                it.addBatch('Yu Fil Se Na Fulish', 2, 19);

                it.addBatch('Friend Zone', 6, 20);
                it.addBatch('Sade', 16, 20);
                it.addBatch('Mr. Foolish', 3, 21);

                it.addBatch('JA ARA E', 8, 22);
                it.addBatch('African Giant', 1, 23);
                it.addBatch('Anyboy', 2, 23);
                it.addBatch('On The Low', 16, 23);

                it.addBatch('Zombie', 5, 24);
                it.addBatch('Water No Get Enemy', 4, 24);
                it.addBatch('Lady', 1, 24);

                it.addBatch('Drinking Drinks', 3, 25);
                it.addBatch('Mind your Baynay', 1, 26);
                it.addBatch('Premier Gaou', 1, 27);
                it.addBatch('Spoil You With Love', 5, 28);
            })
        } catch (ClassNotFoundException e) {
            log.error("Unable to find Driver Class: {} - {}", e.getMessage(), e)
            throw e
        } catch (IllegalAccessException | SQLException e) {
            log.error("Failed to Connect to Database: {} - {}", e.getMessage(), e)
            throw e
        }
    }

//    Connection getConnection() {
//        return this.connection
//    }

    /**
     * Disconnect from the Datasource.
     */
    void disconnect() {
        try {
            if (sql != null) {
                sql.close()
            } else
                log.error("Error, no Connection established!")
        } catch (Exception e) {
            log.error("Failed to close the connect", e)
            throw e
        }
    }

    /**
     * Perform rollback
     */
    void performRollback() {
        try {
            log.warn("Performing rollback!")
            sql.rollback()
        } catch (SQLException e) {
            log.error("Failed to perform rollback: {}", e.getMessage(), e)
        }
    }

    /**
     * Executes a sql query.
     * @param callableStatement the sql statement
     * @param message the log message
     * @return the row count, or last insert ID
     * @throws SQLException if an error occurs
     */
    int executeSql(String query, List<Object> params) throws SQLException {
        sql.withTransaction {conn ->
            {

            }
        }
    }

}
