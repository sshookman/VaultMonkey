package codepoet.vaultmonkey;

@SqliteObject(table = "test")
public class TestObject {

	@SqliteColumn
	private Integer id;

	@SqliteColumn
	private String name;

	@SqliteColumn
	private Boolean good;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getGood() {
		return good;
	}

	public void setGood(Boolean good) {
		this.good = good;
	}
}
