package codepoet.vaultmonkey;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqliteObjectMapper<D> {

	private Class<D> clazz;

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
				dataMap.put(key, value != null ? value.toString() : "NULL");
			}
		}

		return dataMap;
	}

	public D mapResultSetToObject(ResultSet results) throws Exception {
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
}
