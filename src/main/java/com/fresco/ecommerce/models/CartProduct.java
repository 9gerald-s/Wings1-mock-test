package com.fresco.ecommerce.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id","product_id"}))
@Entity
public class CartProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cpId;
	@ManyToOne()
	@JoinColumn(name = "cart_id",referencedColumnName = "cartId")
	@JsonIgnore
	private Cart cart;
	@ManyToOne
	@JoinColumn(name = "product_id",referencedColumnName = "productId")
	private Product product;
	private Integer quantity=1;
	public Integer getCpId() {
		return cpId;
	}
	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public CartProduct(Integer cpId, Cart cart, Product product, Integer quantity) {
		super();
		this.cpId = cpId;
		this.cart = cart;
		this.product = product;
		this.quantity = quantity;
	}
	public CartProduct() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CartProduct [cpId=" + cpId + ", cart=" + cart + ", product=" + product + ", quantity=" + quantity + "]";
	}
	
	
}
