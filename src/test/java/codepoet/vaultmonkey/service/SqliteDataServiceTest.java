package codepoet.vaultmonkey.service;

import codepoet.vaultmonkey.util.SqliteConnectionUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class SqliteDataServiceTest {

	private static final String FILE = "src/test/resources/library/test.sqlite";
	private static final String INIT_FILE = "init/init_test.sql";
	private static Connection connection;
	private static SqliteDataService<TestObject> testDataService;

	@Before
	public void setup() throws Exception {
		connection = SqliteConnectionUtil.establishConnection(FILE);
		init(connection, INIT_FILE);
		testDataService = new SqliteDataService<>(TestObject.class, connection);
	}

	@AfterClass
	public static void teardown() {
		File file = new File(FILE);
		file.delete();
	}

	@Test
	public void testCRUD() throws Exception {
		testRead_Map_Empty();
		testCreate();
		testRead_Integer(); //Failing Here - Not finding record
		testUpdate();
		testRead_Map();
		testDelete();
		testRead_Map_Empty();

		//Test Errors
		Integer noInt = null;
		Map<String, String> noMap = null;
		assertNull(testDataService.read(noInt));
		assertNull(testDataService.read(noMap));
		assertNull(testDataService.create(null));
		assertNull(testDataService.update(null, new TestObject()));
		assertNull(testDataService.update(1, null));
		assertNull(testDataService.delete(null));
		assertTrue(testDataService.read(new HashMap<>()).isEmpty());
		assertFalse(testDataService.create(new TestObject()));
	}

	public void testRead_Map_Empty() {
		List<TestObject> objects = testDataService.read(new HashMap<>());
		assertNotNull(objects);
		assertTrue(objects.isEmpty());
	}

	public void testRead_Map() {
		List<TestObject> objects = testDataService.read(new HashMap<>());
		assertNotNull(objects);
		assertEquals(1, objects.size());

		TestObject object = objects.get(0);
		assertNotNull(object);
		assertEquals(Integer.valueOf(1), object.getId());
		assertEquals("Eman", object.getName());
		assertEquals(Double.valueOf(12.14), object.getDubs());
		assertEquals(false, object.getGood());
		assertEquals(Long.valueOf(654321), object.getLoooooooong());
	}

	public void testRead_Integer() {
		TestObject object = (TestObject) testDataService.read(1);
		assertNotNull(object);
		assertEquals(Integer.valueOf(1), object.getId());
		assertEquals("Bob", object.getName());
		assertEquals(Double.valueOf(12.12), object.getDubs());
		assertEquals(false, object.getGood());
		assertEquals(Long.valueOf(123456), object.getLoooooooong());

		assertNull(testDataService.read(2));
	}

	public void testCreate() throws Exception {
		TestObject object = new TestObject();
		object.setId(1);
		object.setName("Bob");
		object.setDubs(12.12);
		object.setLoooooooong(123456L);
		object.setGood(true);
		assertTrue(testDataService.create(object));
	}

	public void testUpdate() throws Exception {
		TestObject object = new TestObject();
		object.setId(1);
		object.setName("Eman");
		object.setDubs(12.14);
		object.setLoooooooong(654321L);
		object.setGood(false);
		assertTrue(testDataService.update(1, object));
	}

	public void testDelete() {
		assertTrue(testDataService.delete(1));
	}

	private Connection init(final Connection connection, final String fileName) throws FileNotFoundException, SQLException {
		String query = getFile(fileName);
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		statement.close();
		return connection;
	}

	private String getFile(String fileName) throws FileNotFoundException {
		StringBuilder result = new StringBuilder("");
		File file = load(fileName);
		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine()) {
			result.append(scanner.nextLine()).append("\n");
		}

		return result.toString();
	}

	private File load(final String resource) {
		try {
			return new File(getClass().getClassLoader().getResource(resource).getFile());
		} catch (Exception exception) {
			return null;
		}
	}
}
