package codepoet.vaultmonkey.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqliteObjectMapperTest {

	private static SqliteObjectMapper<TestObject> mapper;
	private static ResultSet mockResultSet;

	@BeforeClass
	public static void setup() throws SQLException {

		mapper = new SqliteObjectMapper<>(TestObject.class);
		mockResultSet = mock(ResultSet.class);

		when(mockResultSet.getInt(anyString())).thenReturn(123);
		when(mockResultSet.getString(anyString())).thenReturn("This");
		when(mockResultSet.getBoolean(anyString())).thenReturn(false);
		when(mockResultSet.getDouble(anyString())).thenReturn(12.12);
		when(mockResultSet.getLong(anyString())).thenReturn(4746L);
	}

	@Test
	public void testMapObjectToMap() throws Exception {

		TestObject testObject = new TestObject();
		testObject.setId(1);
		testObject.setName("HEY");
		testObject.setGood(true);
		testObject.setDubs(12.34);
		testObject.setLoooooooong(123456L);

		Map<String, String> dataMap = mapper.mapObjectToMap(testObject);
		assertNotNull(dataMap);
		assertEquals("1", dataMap.get("id"));
		assertEquals("HEY", dataMap.get("name"));
		assertEquals("true", dataMap.get("good"));
		assertEquals("12.34", dataMap.get("dubs"));
		assertEquals("123456", dataMap.get("loooooooong"));
	}

	@Test
	public void testMapResultSetToObject() throws Exception {

		TestObject response = mapper.mapResultSetToObject(mockResultSet);
		assertNotNull(response);
		assertEquals(Integer.valueOf(123), response.getId());
		assertEquals("This", response.getName());
		assertEquals(false, response.getGood());
		assertEquals(Double.valueOf(12.12), response.getDubs());
		assertEquals(Long.valueOf(4746L), response.getLoooooooong());
	}
}
