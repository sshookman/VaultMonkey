package codepoet.vaultmonkey.service;


import codepoet.vaultmonkey.annotations.SqliteColumn;
import codepoet.vaultmonkey.annotations.SqliteObject;

@SqliteObject(table = "test")
public class TestObject {

	@SqliteColumn
	private Integer id;

	@SqliteColumn
	private String name;

	@SqliteColumn
	private Boolean good;

	@SqliteColumn
	private Double dubs;

	@SqliteColumn
	private Long loooooooong;

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

	public Double getDubs() {
		return dubs;
	}

	public void setDubs(Double dubs) {
		this.dubs = dubs;
	}

	public Long getLoooooooong() {
		return loooooooong;
	}

	public void setLoooooooong(Long loooooooong) {
		this.loooooooong = loooooooong;
	}
}
