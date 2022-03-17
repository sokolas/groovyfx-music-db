package groovyfx.music.db.services.impl

import groovy.util.logging.Slf4j
import groovyfx.music.db.config.Datasource;
import java.sql.Connection;

@Slf4j
abstract class AbstractDAOImpl {
    Connection connection;

    AbstractDAOImpl() {
        try {
            this.connection = Datasource.INSTANCE.getConnection();
        } catch (Exception e) {
            log.error("Failed to connect to Datasource. - ${e.getMessage()}", e);
            throw e;
        }
    }
}
