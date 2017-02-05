package codepoet.vaultmonkey.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteConnectionManager {

	private static final Logger LOGGER = Logger.getLogger(SqliteConnectionManager.class.getName());

	public static Connection establishConnection(final String dbFile) throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (ClassNotFoundException | SQLException exception) {
			LOGGER.log(Level.SEVERE, "Failed to Connect to Database: {0}", exception.getMessage());
			throw exception;
		}
	}

	public static Connection establishConnectionInMemory(final String dbFile) throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate("ATTACH DATABASE '" + dbFile + "' AS 'aux'");
			}
			return connection;
		} catch (ClassNotFoundException | SQLException exception) {
			LOGGER.log(Level.SEVERE, "Failed to Connect to Database: {0}", exception.getMessage());
			throw exception;
		}
	}
}
