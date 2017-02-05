package codepoet.vaultmonkey.service;

public class QueryConstants {

	public static final String SELECT_TEMPLATE = "SELECT * FROM {TABLE}{WHERE};";
	public static final String INSERT_TEMPLATE = "INSERT INTO {TABLE} ({FIELDS}) VALUES ({VALUES});";
	public static final String UPDATE_TEMPLATE = "UPDATE {TABLE} SET {SET}{WHERE};";
	public static final String DELETE_TEMPLATE = "DELETE FROM {TABLE}{WHERE};";
	
	public static final String WHERE = " WHERE ";
	public static final String AND = " AND ";
}
