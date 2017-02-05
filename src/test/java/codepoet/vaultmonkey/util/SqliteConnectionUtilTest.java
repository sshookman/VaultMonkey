package codepoet.vaultmonkey.util;

import java.io.File;
import java.sql.Connection;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class SqliteConnectionUtilTest {

	private static final String PATH = "src/test/resources/library/";

	@AfterClass
	public static void teardown() {
		File file = new File(PATH + "/Test.sqlite");
		file.delete();
	}

	@Test
	public void testInstantiate() {
		assertNotNull(new SqliteConnectionUtil());
	}

	@Test
	public void testNotFoundException() throws Exception {
		try {
			SqliteConnectionUtil.establishConnection("not-a-library/NotAFile.sqlite");
			fail("Exception Expected");
		} catch (Exception exception) {
			assertNotNull(exception);
		}

		try {
			SqliteConnectionUtil.establishConnectionInMemory("not-a-library/NotAFile.sqlite");
			fail("Exception Expected");
		} catch (Exception exception) {
			assertNotNull(exception);
		}
	}

	@Test
	public void testEstablishConnectionInMemory() throws Exception {
		Connection memory = SqliteConnectionUtil.establishConnectionInMemory(PATH + "Test.sqlite");
		assertNotNull(memory);
	}
}
