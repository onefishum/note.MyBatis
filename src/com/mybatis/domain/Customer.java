package com.mybatis.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 客户实体
 * 
 * @author onefish
 * 
 */
public class Customer {
	private int id;
	private String name;
	private String address;
	private Integer age;

	private Set<Order> orders = new HashSet<Order>();

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

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", address=" + address
				+ ", age=" + age + ", orders=" + orders + "]";
	}
}
