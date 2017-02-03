package codepoet.vaultmonkey;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataService<D> {

	private static final Logger LOGGER = Logger.getLogger(DataService.class.getName());

	private Class<D> clazz;
	private String tableName;
	private final Connection connection;

	public DataService(final Class<D> clazz, final Connection connection) {
		if (!clazz.isAnnotationPresent(SqliteObject.class)) {
			throw new RuntimeException("Class Must Be @SqliteObject");
		}

		this.clazz = clazz;
		this.tableName = clazz.getAnnotation(SqliteObject.class).table();
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
				D dataObject = map(results);
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
				.value(toMap(dataObject))
				.build();

		return executeUpdate(query);
	}

	public Boolean update(final Integer rowId, final D dataObject) throws Exception {
		if (rowId == null || dataObject == null) {
			return null;
		}

		String idString = rowId.toString();
		String query = new QueryBuilder.UpdateQuery(tableName)
				.set(toMap(dataObject))
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

	public D map(ResultSet results) throws Exception {
		D dataObject = clazz.newInstance();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(SqliteColumn.class)) {
				field.setAccessible(true);
				field.set(dataObject, getValue(field, results));
			}
		}

		return dataObject;
	}

	public static Object getValue(Field field, ResultSet results) throws SQLException {
		String fieldName = field.getAnnotation(SqliteColumn.class).name();
		if (field.getType().isAssignableFrom(Integer.class)) {
			return results.getInt(fieldName);
		} else if (field.getType().isAssignableFrom(String.class)) {
			return results.getString(fieldName);
		} else if (field.getType().isAssignableFrom(Boolean.class)) {
			return results.getBoolean(fieldName);
		} else if (field.getType().isAssignableFrom(Double.class)) {
			return results.getDouble(fieldName);
		} else if (field.getType().isAssignableFrom(Long.class)) {
			return results.getLong(fieldName);
		}

		return null;
	}

	public Map<String, String> toMap(D dataObject) throws Exception {
		Map<String, String> dataMap = new HashMap<>();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(SqliteColumn.class)) {
				field.setAccessible(true);
				String fieldName = field.getAnnotation(SqliteColumn.class).name();
				String key = fieldName.isEmpty() ? field.getName() : fieldName;
				Object value = field.get(dataObject);
				dataMap.put(key, value != null ? value.toString() : "NULL");
			}
		}

		return dataMap;
	}
}
