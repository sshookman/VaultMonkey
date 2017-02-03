package codepoet.vaultmonkey;

import java.util.Map;

public class Main {

	public static void main(String[] args) throws Exception {

		SqliteObjectMapper<TestObject> mapper = new SqliteObjectMapper<>(TestObject.class);

		TestObject testObject = new TestObject();
		testObject.setId(1);
		testObject.setName("HEY");
		testObject.setGood(true);

		Map<String, String> dataMap = mapper.mapObjectToMap(testObject);

		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
