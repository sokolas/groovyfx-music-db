package groovyfx.music.db.services.impl

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import groovyfx.music.db.config.Datasource

@Slf4j
abstract class AbstractDAOImpl {
    Sql sql

    AbstractDAOImpl() {
        try {
            this.sql = Datasource.INSTANCE.sql
        } catch (Exception e) {
            log.error("Failed to connect to Datasource. - ${e.getMessage()}", e)
            throw e
        }
    }
}
