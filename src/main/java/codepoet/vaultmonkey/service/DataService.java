package codepoet.vaultmonkey.service;

import codepoet.vaultmonkey.annotations.SqliteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataService<D> {

	private static final Logger LOGGER = Logger.getLogger(DataService.class.getName());

	private String tableName;
	private SqliteObjectMapper<D> mapper;
	private final Connection connection;

	public DataService(final Class<D> clazz, final Connection connection) {
		if (!clazz.isAnnotationPresent(SqliteObject.class)) {
			throw new RuntimeException("Class Must Be @SqliteObject");
		}

		this.tableName = clazz.getAnnotation(SqliteObject.class).table();
		this.mapper = new SqliteObjectMapper<>(clazz);
		this.connection = connection;
	}

	private Boolean executeUpdate(final String query) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
			return true;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, "Failed to execute query: {0}", query);
			LOGGER.log(Level.SEVERE, exception.getMessage());
			return false;
		}
	}

	private List<D> executeQuery(final String query) {
		List<D> dataObjects = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				@SuppressWarnings("unchecked")
				D dataObject = mapper.mapResultSetToObject(results);
				dataObjects.add(dataObject);
			}
			results.close();
			statement.close();
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, "Failed to execute query: {0}", query);
			LOGGER.log(Level.SEVERE, exception.getMessage());
		}
		return dataObjects;
	}

	public List<D> read(Map<String, String> search) {
		if (search == null) {
			return null;
		}

		String query = new QueryBuilder.SelectQuery(tableName)
				.whereEquals(search)
				.build();

		return executeQuery(query);
	}

	public D read(final Integer rowId) {
		if (rowId == null) {
			return null;
		}

		String idString = rowId.toString();
		String query = new QueryBuilder.SelectQuery(tableName)
				.whereEquals("id", idString)
				.build();

		List<D> results = executeQuery(query);
		return results.isEmpty() ? null : results.get(0);
	}

	public Boolean create(D dataObject) throws Exception {
		if (dataObject == null) {
			return null;
		}

		String query = new QueryBuilder.InsertQuery(tableName)
				.value(mapper.mapObjectToMap(dataObject))
				.build();

		return executeUpdate(query);
	}

	public Boolean update(final Integer rowId, final D dataObject) throws Exception {
		if (rowId == null || dataObject == null) {
			return null;
		}

		String idString = rowId.toString();
		String query = new QueryBuilder.UpdateQuery(tableName)
				.set(mapper.mapObjectToMap(dataObject))
				.whereEquals("id", idString)
				.build();

		return executeUpdate(query);
	}

	public Boolean delete(final Integer rowId) {
		if (rowId == null) {
			return null;
		}

		String idString = rowId.toString();
		String query = new QueryBuilder.DeleteQuery(tableName)
				.whereEquals("id", idString)
				.build();

		return executeUpdate(query);
	}
}
