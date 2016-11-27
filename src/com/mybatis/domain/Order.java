package com.mybatis.domain;
/**
 * 订单
 * @author onefish
 *
 */
public class Order {
	private int id;
	private String orderNumber;
	private Double price;
	
	private Customer customer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double priceDouble) {
		this.price= priceDouble;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNumber=" + orderNumber
				+ ", price=" + price+ ", customer=" + customer
				+ "]";
	}
}
