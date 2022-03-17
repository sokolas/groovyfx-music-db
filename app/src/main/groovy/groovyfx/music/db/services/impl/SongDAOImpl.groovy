package groovyfx.music.db.services.impl

import groovy.util.logging.Slf4j
import groovyfx.music.db.config.Datasource
import groovyfx.music.db.models.Song
import groovyfx.music.db.services.SongDAO

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import static groovyfx.music.db.constants.DbConstants.*

@Slf4j
class SongDAOImpl extends AbstractDAOImpl implements SongDAO {
    private static final String SELECT_ALL_SONGS = String.format("SELECT * FROM %s ORDER BY %s", TABLE_SONGS, COLUMN_SONG_ID);
    private static final String SELECT_SONG_BY_ID = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_SONGS, COLUMN_SONG_ID);
    private static final String SELECT_SONG_BY_TITLE = String.format("SELECT * FROM %s WHERE %s = ?", TABLE_SONGS, COLUMN_SONG_TITLE);
    private static final String INSERT_SONG = String.format("INSERT INTO %s(%s,%s,%s) VALUES(?,?,?)", TABLE_SONGS, COLUMN_SONG_TITLE, COLUMN_SONG_TRACK, COLUMN_SONG_ALBUM_ID);
    private static final String UPDATE_SONG = String.format("UPDATE %s SET %s = ?,%s =?,%s=? WHERE %s = ?", TABLE_SONGS, COLUMN_SONG_TITLE, COLUMN_SONG_TRACK, COLUMN_SONG_ALBUM_ID, COLUMN_SONG_ID);
    private static final String DELETE_SONG = String.format("DELETE FROM %s WHERE %s = ?", TABLE_SONGS, COLUMN_SONG_ID);

    @Override
    List<Song> findAll() {
        List<Song> songs = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(SELECT_ALL_SONGS)) {

            while (results.next()) {
                Song song = new Song(
                        results.getInt(INDEX_SONG_ID),
                        results.getString(INDEX_SONG_TITLE),
                        results.getInt(INDEX_SONG_TRACK),
                        results.getInt(INDEX_SONG_ALBUM_ID)
                );
                songs.add(song);
            }
            log.info("Found {} songs(s).", songs.size());
            return songs;
        } catch (SQLException e) {
            log.error("SELECT ALL Songs query failed: {}", e.getMessage(), e);
            return songs;
        }
    }

    @Override
    Optional<Song> findOne(Integer id) {
        Optional<Song> song = Optional.empty();
        try (PreparedStatement queryAlbum = connection.prepareStatement(SELECT_SONG_BY_ID)) {
            queryAlbum.setInt(1, id);
            ResultSet results = queryAlbum.executeQuery();

            if (results.next()) {
                song = Optional.of(new Song(id, results.getString(INDEX_SONG_TITLE), results.getInt(INDEX_SONG_TRACK), results.getInt(INDEX_SONG_ALBUM_ID)));
                log.info("Found: {}", song.get());
            } else {
                log.error("Failed to find Song #{}", id);
            }
            results.close();
            return song;
        } catch (SQLException e) {
            log.error("Find Song by ID query failed: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    Song findByName(String name) {
        Song song = null;
        try (PreparedStatement querySong = connection.prepareStatement(SELECT_SONG_BY_TITLE)) {
            querySong.setString(1, name);
            ResultSet results = querySong.executeQuery();

            if (results.next()) {
                song = new Song(results.getInt(INDEX_SONG_ID), name, results.getInt(INDEX_SONG_TRACK), results.getInt(INDEX_SONG_ALBUM_ID));
                log.info("Found: {}", song);
            } else {
                log.error("Failed to find Song titled '{}'", name);
            }
            results.close();
            return song;
        } catch (SQLException e) {
            log.error("Find Song by Album Name failed: {}", e.getMessage(), e);
            return song;
        }
    }

    @Override
    Song save(Song song) {
        Song saved = null;
        try (PreparedStatement insertIntoSongs = connection.prepareStatement(INSERT_SONG, Statement.RETURN_GENERATED_KEYS)) {
            insertIntoSongs.setString(1, song.title());
            insertIntoSongs.setInt(2, song.track());
            insertIntoSongs.setInt(3, song.albumID());

            int rowsAffected = insertIntoSongs.executeUpdate();
            if (rowsAffected == 1) {
                log.info("Rows affected: {}", rowsAffected);

                ResultSet resultSet = insertIntoSongs.getGeneratedKeys();
                if (resultSet.next()) {
                    saved = new Song(resultSet.getInt(INDEX_SONG_ID), song.title(), song.track(), song.albumID());
                    log.info("Saved: {}", saved);
                    this.connection.commit();
                }
                resultSet.close();
            }
            return saved;
        } catch (SQLException e) {
            log.error("INSERT Song failed: {}", e.getMessage(), e);
            Datasource.INSTANCE.performRollback();
            return null;
        }
    }

    @Override
    Song update(Song song) {
        try (PreparedStatement updateSong = connection.prepareStatement(UPDATE_SONG, Statement.RETURN_GENERATED_KEYS)) {
            if (song != null) {
                updateSong.setString(1, song.title());
                updateSong.setInt(2, song.track());
                updateSong.setInt(3, song.albumID());
                updateSong.setInt(4, song.id());

                int rowsAffected = updateSong.executeUpdate();
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected);
                    this.connection.commit();
                    log.info("Updated Song #{}", song.id());
                }
                return song;
            } else {
                throw new IllegalArgumentException("Song not found!");
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("UPDATE Song failed: {}", e.getMessage(), e);
            Datasource.INSTANCE.performRollback();
            return null;
        }
    }

    @Override
    void delete(Integer id) {
        try (PreparedStatement deleteSong = connection.prepareStatement(DELETE_SONG)) {
            if (id != null && id > 0) {
                deleteSong.setInt(1, id);

                int rowsAffected = deleteSong.executeUpdate();
                if (rowsAffected == 1) {
                    log.info("Rows affected: {}", rowsAffected);
                    this.connection.commit();
                    log.info("Deleted Song #{}", id);
                }
            } else {
                throw new IllegalArgumentException("Song not found!");
            }
        } catch (IllegalArgumentException | SQLException e) {
            log.error("DELETE Song failed: {}", e.getMessage(), e);
            Datasource.INSTANCE.performRollback();
        }
    }
}
