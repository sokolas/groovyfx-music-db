package groovyfx.music.db.services.impl

import groovy.util.logging.Slf4j
import groovyfx.music.db.models.Artist
import groovyfx.music.db.services.ArtistDAO

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static groovyfx.music.db.constants.DbConstants.*

@Slf4j
class ArtistDAOImpl extends AbstractDAOImpl implements ArtistDAO {
    private static final String SELECT_ARTISTS_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_ARTISTS, COLUMN_ARTIST_ID)
    private static final String SELECT_ARTISTS_BY_NAME = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_ARTISTS, COLUMN_ARTIST_NAME)
    private static final String SELECT_ALL_ARTISTS = String.format("SELECT * FROM %s ORDER BY %s", TABLE_ARTISTS, COLUMN_ARTIST_NAME)
    private static final String INSERT_ARTIST = String.format("INSERT INTO %s(%s) VALUES(?)", TABLE_ARTISTS, COLUMN_ARTIST_NAME)
    private static final String UPDATE_ARTIST_NAME = String.format("UPDATE %s SET %s = ? WHERE %s = ?", TABLE_ARTISTS, COLUMN_ARTIST_NAME, COLUMN_ARTIST_ID)
    private static final String DELETE_ARTIST = String.format("DELETE FROM %s WHERE %s = ?", TABLE_ARTISTS, COLUMN_ARTIST_ID)

    ArtistDAOImpl() {
        super()
    }

    @Override
    List<Artist> findAll() {
        try {
            return sql.rows(SELECT_ALL_ARTISTS).collect {
                def artist = new Artist(it.id, it.name)
                log.debug(artist.toString())
                artist
            }
        } catch (Exception e) {
            log.error("SELECT ALL Artists query failed: {}", e.getMessage(), e)
            return new ArrayList<>()
        }
    }

    private void delay() throws InterruptedException {
        try {
            //Intentional delay so we can see the progress bar.
            Thread.sleep(40)
        } catch (InterruptedException e) {
            log.error("An error occurred - ${e.message}", e)
            e.printStackTrace()
            throw e
        }
    }

    @Override
    Optional<Artist> findOne(Integer id) {
        Optional<Artist> artist = Optional.empty()
        try {
            def results = sql
                .rows(SELECT_ARTISTS_BY_ID)

            if (results.size() == 1) {
                artist = Optional.of(new Artist(id, results.getString(INDEX_ARTIST_NAME)))
                log.info("Found: {}", artist.get())
            } else {
                log.error("Failed to find Artist #{}", id)
            }
            results.close()
            return artist
        } catch (SQLException e) {
            log.error("Find Artist by ID query failed: $e.message", e)
            return Optional.empty()
        }
    }

    @Override
    Artist findByName(String artistName) {
        Artist artist = null
        try (PreparedStatement queryArtist = connection.prepareStatement(SELECT_ARTISTS_BY_NAME)) {
            queryArtist.setString(1, artistName)
            ResultSet results = queryArtist.executeQuery()

            if (results.next()) {
                artist = new Artist(results.getInt(INDEX_ARTIST_ID), results.getString(INDEX_ARTIST_NAME))
                log.info("Found: {}", artist)
            } else {
                log.error("Failed to find Artist '{}'", artistName)
            }
            results.close()
            return artist
        } catch (SQLException e) {
            log.error("Find Artist by Name query failed: $e.message", e)
            return null
        }
    }

    @Override
    Artist save(Artist artist) {
        Artist saved = null
        try (PreparedStatement insertIntoArtists = connection.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS)) {
            insertIntoArtists.setString(1, artist.getName())

            int rowsAffected = insertIntoArtists.executeUpdate()
            if (rowsAffected == 1) {
                log.info("Rows affected: {}", rowsAffected)

                ResultSet resultSet = insertIntoArtists.getGeneratedKeys()
                if (resultSet.next()) {
                    saved = new Artist(resultSet.getInt(INDEX_ARTIST_ID), artist.getName())
                    log.info("Saved: {}", saved)
                    this.connection.commit()
                }
                resultSet.close()
            }
            return saved
        } catch (SQLException e) {
            log.error("INSERT Artist failed: $e.message", e)
            rollback()
            return saved
        }
    }

    @Override
    Artist update(Artist artist) {
        try (PreparedStatement updateArtists = connection.prepareStatement(UPDATE_ARTIST_NAME, Statement.RETURN_GENERATED_KEYS)) {
            if (artist != null) {
                updateArtists.setString(1, artist.getName())
                updateArtists.setInt(2, artist.getId())

                int rowsAffected = updateArtists.executeUpdate()
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected)
                    this.connection.commit()
                    log.info("Updated Artist #{}", artist.getId())
                }
                return artist
            } else {
                throw new IllegalArgumentException("Artist not found!")
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("UPDATE Artist failed: {}", e.getMessage(), e)
            rollback()
            return null
        }
    }

    @Override
    void delete(Integer id) {
        try (PreparedStatement deleteArtist = connection.prepareStatement(DELETE_ARTIST)) {
            if (id != null && id > 0) {
                deleteArtist.setInt(1, id)

                int rowsAffected = deleteArtist.executeUpdate()
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected)
                    this.connection.commit()
                    log.info("Delete Artist #{}", id)
                }
            } else {
                throw new IllegalArgumentException("Artist not found!")
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("DELETE Artist failed: {}", e.getMessage(), e)
            rollback()
        }
    }
}
