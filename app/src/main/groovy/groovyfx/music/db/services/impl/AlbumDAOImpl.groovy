package groovyfx.music.db.services.impl

import groovy.sql.GroovyRowResult
import groovy.util.logging.Slf4j
import groovyfx.music.db.config.Datasource
import groovyfx.music.db.models.Album
import groovyfx.music.db.services.AlbumDAO

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static groovyfx.music.db.constants.DbConstants.*

@Slf4j
class AlbumDAOImpl extends AbstractDAOImpl implements AlbumDAO {
    private static final String SELECT_ALL_ALBUMS = String.format("SELECT * FROM %s ORDER BY %s", TABLE_ALBUMS, COLUMN_ALBUM_ID)
    /*
       private static final String DOT = String.valueOf('.');
       private static final String SELECT_ALL_ALBUMS = """
           SELECT al%s%s, al%s%s, ar%s%s
           FROM (%s al
           INNER JOIN %s ar
           ON al%s%s = ar%s%s)
           ORDER BY al%s%s
           """.formatted(DOT, COLUMN_ALBUM_ID,
               DOT, COLUMN_ALBUM_NAME,
               DOT, COLUMN_ARTIST_NAME,
               TABLE_ALBUMS, TABLE_ARTISTS,
               DOT,COLUMN_ALBUM_ID,
               DOT, COLUMN_ARTIST_ID,
               DOT, COLUMN_ALBUM_ID);
               */

    private static final String SELECT_ALBUMS_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_ALBUMS, COLUMN_ALBUM_ID)
    private static final String SELECT_ALBUMS_BY_NAME = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_ALBUMS, COLUMN_ALBUM_NAME)
    private static final String SELECT_ALBUMS_BY_ARTIST_ID = String.format("SELECT * FROM %s WHERE %s = ? ORDER BY %s", TABLE_ALBUMS, COLUMN_ALBUM_ARTIST_ID, COLUMN_ALBUM_NAME)
    private static final String INSERT_ALBUM = String.format("INSERT INTO %s(%s, %s) VALUES(?, ?)", TABLE_ALBUMS, COLUMN_ALBUM_NAME, COLUMN_ALBUM_ARTIST_ID)
    private static final String UPDATE_ALBUM = String.format("UPDATE %s SET %s = ?, %s =? WHERE %s = ?", TABLE_ALBUMS, COLUMN_ALBUM_NAME, COLUMN_ALBUM_ARTIST_ID, COLUMN_ALBUM_ID)
    private static final String DELETE_ALBUM = String.format("DELETE FROM %s WHERE %s = ?", TABLE_ALBUMS, COLUMN_ALBUM_ID)

    @Override
    List<Album> findAll() {
        try {
            sql
                    .rows(SELECT_ALL_ALBUMS)
                    .collect {toAlbum(it)}
                    .each {log.debug(it.toString())}
        } catch (SQLException e) {
            log.error("SELECT ALL ALBUMS query failed: {}", e.getMessage(), e)
            return Collections.emptyList()
        }
    }

    @Override
    List<Album> findAllByArtistId(Integer artistId) {
        try {
            sql
                    .rows(SELECT_ALBUMS_BY_ARTIST_ID, artistId)
                    .collect {toAlbum(it)}
                    .each {log.debug(it.toString())}
        } catch (SQLException e) {
            log.error("SELECT ALL Albums by Artist ID query failed: {}", e.getMessage(), e)
            return albums
        }
    }

    @Override
    Optional<Album> findOne(Integer id) {
        Optional<Album> album = Optional.empty()
        try (PreparedStatement queryAlbum = connection.prepareStatement(SELECT_ALBUMS_BY_ID)) {
            queryAlbum.setInt(1, id)
            ResultSet results = queryAlbum.executeQuery()

            if (results.next()) {
                album = Optional.of(new Album(id, results.getInt(INDEX_ALBUM_ARTIST_ID), results.getString(INDEX_ALBUM_NAME)))
                log.info("Found: {}", album.get())
            } else {
                log.error("Failed to find Album #{}", id)
            }
            results.close()
            return album
        } catch (SQLException e) {
            log.error("Find Album by ID query failed: {}", e.getMessage(), e)
            return Optional.empty()
        }
    }

    @Override
    Album findByName(String albumName) {
        Album album = null
        try (PreparedStatement queryAlbum = connection.prepareStatement(SELECT_ALBUMS_BY_NAME)) {
            queryAlbum.setString(1, albumName)
            ResultSet results = queryAlbum.executeQuery()

            if (results.next()) {
                album = new Album(results.getInt(INDEX_ALBUM_ID), results.getInt(INDEX_ALBUM_ARTIST_ID), albumName)
                log.info("Found: {}", album)
            } else {
                log.error("Failed to find Album '{}'", albumName)
            }
            results.close()
            return album
        } catch (SQLException e) {
            log.error("Find Artist by Album query failed: {}", e.getMessage(), e)
            return album
        }
    }

    @Override
    Album save(Album album) {
        Album saved = null
        try (PreparedStatement insertIntoAlbums = connection.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS)) {
            insertIntoAlbums.setString(1, album.getName())
            insertIntoAlbums.setInt(2, album.getArtistId())

            int rowsAffected = insertIntoAlbums.executeUpdate()
            if (rowsAffected == 1) {
                log.info("Rows affected: {}", rowsAffected)

                ResultSet resultSet = insertIntoAlbums.getGeneratedKeys()
                if (resultSet.next()) {
                    saved = new Album(resultSet.getInt(INDEX_ALBUM_ID), album.getArtistId(), album.getName())
                    log.info("Saved: {}", saved)
                    this.connection.commit()
                }
                resultSet.close()
            }
            return saved
        } catch (SQLException e) {
            log.error("INSERT Album failed: {}", e.getMessage(), e)
            rollback()
            return saved
        }
    }

    @Override
    Album update(Album album) {
        try (PreparedStatement updateAlbum = connection.prepareStatement(UPDATE_ALBUM, Statement.RETURN_GENERATED_KEYS)) {
            if (album != null) {
                updateAlbum.setString(1, album.getName())
                updateAlbum.setInt(2, album.getArtistId())
                updateAlbum.setInt(3, album.getId())

                int rowsAffected = updateAlbum.executeUpdate()
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected)
                    this.connection.commit()
                    log.info("Updated Album #{}", album.getArtistId())
                }
                return album
            } else {
                throw new IllegalArgumentException("Album not found!")
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("UPDATE Album failed: {}", e.getMessage(), e)
            rollback()
            return null
        }
    }

    @Override
    void delete(Integer id) {
        try (PreparedStatement deleteAlbum = connection.prepareStatement(DELETE_ALBUM)) {
            if (id != null && id > 0) {
                deleteAlbum.setInt(1, id)

                int rowsAffected = deleteAlbum.executeUpdate()
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected)
                    this.connection.commit()
                    log.info("Deleted Album #{}", id)
                }
            } else {
                throw new IllegalArgumentException("ALbum not found!")
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("DELETE Album failed: ${e.message}", e)
            rollback()
        }
    }

    static toAlbum = { GroovyRowResult it ->
        return new Album(it.id, it.artistId, it.name)
    }
}
