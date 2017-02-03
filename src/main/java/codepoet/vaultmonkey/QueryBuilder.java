package codepoet.vaultmonkey;

import static codepoet.vaultmonkey.QueryConstants.*;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {

	private static String buildWhere(final Map<String, String> conditions) {
		StringBuilder whereBuilder = new StringBuilder();

		boolean isFirst = true;
		for (Map.Entry<String, String> entry : conditions.entrySet()) {
			if (isFirst) {
				isFirst = false;
				whereBuilder.append(WHERE);
			} else {
				whereBuilder.append(AND);
			}
			whereBuilder
					.append(entry.getKey())
					.append("=")
					.append(entry.getValue());
		}

		return whereBuilder.toString();
	}

	public static class SelectQuery {

		private String table;
		private Map<String, String> conditions = new HashMap<String, String>();

		public SelectQuery(String table) {
			this.table = table;
		}

		public SelectQuery whereEquals(final String field, final String value) {
			conditions.put(field, value);
			return this;
		}

		public SelectQuery whereEquals(final Map<String, String> fieldValues) {
			conditions.putAll(fieldValues);
			return this;
		}

		public String build() {
			String query = SELECT_TEMPLATE;
			query = query.replace("{TABLE}", table);
			query = query.replace("{WHERE}", buildWhere(conditions));
			return query;
		}
	}

	public static class InsertQuery {

		private String table;
		private StringBuilder fieldsBuilder = new StringBuilder();
		private StringBuilder valuesBuilder = new StringBuilder();

		public InsertQuery(String table) {
			this.table = table;
		}

		public InsertQuery value(final String field, final String value) {
			if (fieldsBuilder.length() != 0) {
				fieldsBuilder.append(",");
			}
			if (valuesBuilder.length() != 0) {
				valuesBuilder.append(",");
			}
			fieldsBuilder.append(field);
			valuesBuilder.append(value);
			return this;
		}

		public InsertQuery value(final Map<String, String> values) {
			for (Map.Entry<String, String> entry : values.entrySet()) {
				value(entry.getKey(), entry.getValue());
			}
			return this;
		}

		public String build() {
			String fields = fieldsBuilder.toString().trim();
			String values = valuesBuilder.toString().trim();

			String query = INSERT_TEMPLATE;
			query = query.replace("{TABLE}", table);
			query = query.replace("{FIELDS}", fields);
			query = query.replace("{VALUES}", values);
			return query;
		}
	}

	public static class UpdateQuery {

		private String table;
		private Map<String, String> conditions = new HashMap<String, String>();
		private StringBuilder setBuilder = new StringBuilder();

		public UpdateQuery(String table) {
			this.table = table;
		}

		public UpdateQuery whereEquals(final String field, final String value) {
			conditions.put(field, value);
			return this;
		}

		public UpdateQuery set(final String field, final String value) {
			if (setBuilder.length() != 0) {
				setBuilder.append(",");
			}
			setBuilder
					.append(field)
					.append("=")
					.append(value);
			return this;
		}

		public UpdateQuery set(final Map<String, String> values) {
			for (Map.Entry<String, String> entry : values.entrySet()) {
				set(entry.getKey(), entry.getValue());
			}
			return this;
		}

		public String build() {
			String set = setBuilder.toString().trim();

			String query = UPDATE_TEMPLATE;
			query = query.replace("{TABLE}", table);
			query = query.replace("{SET}", set);
			query = query.replace("{WHERE}", buildWhere(conditions));
			return query;
		}
	}

	public static class DeleteQuery {

		private String table;
		private Map<String, String> conditions = new HashMap<String, String>();

		public DeleteQuery(String table) {
			this.table = table;
		}

		public DeleteQuery whereEquals(final String field, final String value) {
			conditions.put(field, value);
			return this;
		}

		public String build() {
			String query = DELETE_TEMPLATE;
			query = query.replace("{TABLE}", table);
			query = query.replace("{WHERE}", buildWhere(conditions));
			return query;
		}
	}
}
