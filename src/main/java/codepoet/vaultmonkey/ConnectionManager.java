package codepoet.vaultmonkey;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

	private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
	private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

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

	public static Connection initGolemMudHub(final Connection connection) throws FileNotFoundException, SQLException {
		return init(connection, "GOLEM-MUD-HUB.sql");
	}

	public static Connection initGolem(final Connection connection) throws FileNotFoundException, SQLException {
		return init(connection, "GOLEM.sql");
	}

	public static Connection initSaveGame(final Connection connection) throws FileNotFoundException, SQLException {

		Statement statement = connection.createStatement();
		statement.executeUpdate(
				"DROP TABLE IF EXISTS \"state\";\n"
				+ "\n"
				+ "CREATE TABLE \"state\" (\n"
				+ "        \"id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE,\n"
				+ "        \"location_entity_id\" INTEGER NOT NULL\n"
				+ "    );\n"
				+ "\n"
				+ "INSERT INTO state (location_entity_id) VALUES (1);");

		statement.close();
		return connection;
	}

	private static Connection init(final Connection connection, final String fileName) throws FileNotFoundException, SQLException {
		String query = getFile(fileName);
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		statement.close();
		return connection;
	}

	//TODO: This code doesn't belong here
	private static String getFile(String fileName) throws FileNotFoundException {
		StringBuilder result = new StringBuilder("");
		File file = RESOURCE_LOADER.load(fileName);
		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine()) {
			result.append(scanner.nextLine()).append("\n");
		}

		return result.toString();
	}
}
