package codepoet.vaultmonkey.service;

import codepoet.vaultmonkey.annotations.SqliteColumn;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqliteObjectMapper<D> {

	private final Class<D> clazz;

	public SqliteObjectMapper(Class<D> clazz) {
		this.clazz = clazz;
	}

	public Map<String, String> mapObjectToMap(D dataObject) throws Exception {
		Map<String, String> dataMap = new HashMap<>();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(SqliteColumn.class)) {
				field.setAccessible(true);
				String fieldName = field.getAnnotation(SqliteColumn.class).name();
				String key = fieldName.isEmpty() ? field.getName() : fieldName;
				Object value = field.get(dataObject);
				dataMap.put(key, value != null ? mapValue(value) : "NULL");
			}
		}

		return dataMap;
	}

	public D mapResultSetToObject(ResultSet result) throws Exception {
		D dataObject = clazz.newInstance();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(SqliteColumn.class)) {
				field.setAccessible(true);
				field.set(dataObject, getValue(field, result));
			}
		}

		return dataObject;
	}

	private Object getValue(Field field, ResultSet result) throws SQLException {
		String fieldName = field.getAnnotation(SqliteColumn.class).name();
		fieldName = fieldName.isEmpty() ? field.getName() : fieldName;
		if (field.getType().isAssignableFrom(Integer.class)) {
			return result.getInt(fieldName);
		} else if (field.getType().isAssignableFrom(String.class)) {
			return result.getString(fieldName);
		} else if (field.getType().isAssignableFrom(Boolean.class)) {
			return result.getBoolean(fieldName);
		} else if (field.getType().isAssignableFrom(Double.class)) {
			return result.getDouble(fieldName);
		} else if (field.getType().isAssignableFrom(Long.class)) {
			return result.getLong(fieldName);
		}

		return null;
	}

	private String mapValue(Object value) {
		if (value instanceof String || value instanceof Boolean) {
			return "'" + value.toString() + "'";
		}

		return value.toString();
	}
}
