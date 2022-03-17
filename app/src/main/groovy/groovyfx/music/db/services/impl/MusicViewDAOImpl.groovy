package groovyfx.music.db.services.impl

import groovy.util.logging.Slf4j
import groovyfx.music.db.constants.DbConstants
import groovyfx.music.db.models.MusicView

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

@Slf4j
class MusicViewDAOImpl extends AbstractDAOImpl {
    private static final String SELECT_ARTIST_LIST_VIEW = String.format("SELECT * FROM %s", DbConstants.VIEW_ARTIST_LIST);

    List<MusicView> findAll() {
        List<MusicView> musicViewList = [];
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(SELECT_ARTIST_LIST_VIEW)) {

            while (results.next()) {
                MusicView view = new MusicView(
                        results.getString(1),
                        results.getString(2),
                        results.getInt(3),
                        results.getString(4)
                );
                musicViewList.add(view);
            }
            log.info("Found ${musicViewList.size()} records(s).");
            return musicViewList;
        } catch (SQLException e) {
            log.error("SELECT ALL in VIEW query failed: ${e.message}", e);
            return musicViewList;
        }
    }
}
