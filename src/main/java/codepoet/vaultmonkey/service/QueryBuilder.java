package codepoet.vaultmonkey.service;

import static codepoet.vaultmonkey.service.QueryConstants.*;
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

		private final String table;
		private final Map<String, String> conditions = new HashMap<>();

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

		private final String table;
		private final StringBuilder fieldsBuilder;
		private final StringBuilder valuesBuilder;

		public InsertQuery(String table) {
			this.table = table;
			this.fieldsBuilder = new StringBuilder();
			this.valuesBuilder = new StringBuilder();
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
			values.entrySet().stream().forEach((entry) -> {
				value(entry.getKey(), entry.getValue());
			});
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

		private final String table;
		private final Map<String, String> conditions = new HashMap<>();
		private final StringBuilder setBuilder = new StringBuilder();

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
			values.entrySet().stream().forEach((entry) -> {
				set(entry.getKey(), entry.getValue());
			});
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

		private final String table;
		private final Map<String, String> conditions;

		public DeleteQuery(String table) {
			this.table = table;
			this.conditions = new HashMap<>();
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
