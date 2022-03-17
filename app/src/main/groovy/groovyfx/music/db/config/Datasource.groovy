package groovyfx.music.db.config

import groovy.util.logging.Slf4j

import java.sql.*

@Slf4j
enum Datasource {
    INSTANCE;

    private static final StringBuilder CONNECTION_STRING_BUILDER = new StringBuilder("jdbc:mysql://");
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = ":3313/";
    private static final String DB_NAME = "music_db";
    private static final String DB_USER = "root";
    private static final String DB_PW = "";
    private static final String PK_RETRIEVAL = "allowPublicKeyRetrieval=true";
    private static final String USE_SSL = "useSSL=false";
    private static final String PROCEDURE_BODY = "noAccessToProcedureBodies=true";

    private Connection connection = null;

    /**
     * Connect to the Datasource.
     */
    boolean connect() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            CONNECTION_STRING_BUILDER.append(DB_HOST).append(DB_PORT)
                    .append(DB_NAME).append("?")
                    .append(PROCEDURE_BODY).append("&")
                    .append(PK_RETRIEVAL).append("&")
                    .append(USE_SSL);

            connection = DriverManager.getConnection(CONNECTION_STRING_BUILDER.toString(), DB_USER, DB_PW);

            if (connection != null) {
                log.debug("Successful DB connection.");
                connection.setAutoCommit(false);
                return true;
            } else {
                throw new IllegalAccessException("DB Connection failed!");
            }
        } catch (ClassNotFoundException e) {
            log.error("Unable to find Driver Class: {} - {}", e.getMessage(), e);
            throw e;
        } catch (IllegalAccessException | SQLException e) {
            log.error("Failed to Connect to Database: {} - {}", e.getMessage(), e);
            throw e;
        }
    }

    Connection getConnection() {
        return this.connection;
    }

    /**
     * Disconnect from the Datasource.
     */
    void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            } else
                log.error("Error, no Connection established!");
        } catch (SQLException e) {
            log.error("Failed to close the connect", e);
            throw e;
        }
    }

    /**
     * Perform rollback
     */
    void performRollback() {
        try {
            log.warn("Performing rollback!");
            connection.rollback();
        } catch (SQLException e) {
            log.error("Failed to perform rollback: {}", e.getMessage(), e);
        }
    }

    /**
     * Executes a sql query.
     * @param callableStatement the sql statement
     * @param message the log message
     * @return the row count, or last insert ID
     * @throws SQLException if an error occurs
     */
    int executeSql(CallableStatement callableStatement, String message) throws SQLException {
        if (callableStatement.execute()) {
            try (ResultSet resultSet = callableStatement.getResultSet()) {
                if (resultSet.next()) {
                    log.info(message);
                    connection.commit();
                } else {
                    performRollback();
                }
            }
        }
        return -1;
    }

}
