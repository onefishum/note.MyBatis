package com.mybatis.domain;

public class User {
	private int id;
	private String name;
	private String address;
	private Integer age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", address=" + address
				+ ", age=" + age + "]";
	}

	public User(String name, String address, Integer age) {
		super();
		this.name = name;
		this.address = address;
		this.age = age;
	}

	public User(int id, String name, String address, Integer age) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.age = age;
	}

	public User() {
		super();
	}
}
