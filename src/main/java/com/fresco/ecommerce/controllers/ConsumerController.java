package com.fresco.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.models.Cart;
import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.repo.CartProductRepo;
import com.fresco.ecommerce.repo.CartRepo;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

	@Autowired
	CartRepo cartRepo;
	@Autowired
	ProductRepo productRepo;
	@Autowired
	CartProductRepo cartProductRepo;
	@Autowired
	UserRepo userRepo;

	@GetMapping("/cart")
	public ResponseEntity<Object> getCart() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(userRepo.findByUsername(username));
		return ResponseEntity.ok(cartRepo.findByUserUsername(username));
	}

	@PostMapping("/cart")
	public ResponseEntity<Object> postCart(@RequestBody Product product) {
		System.out.println("gerald"+product.getCategory());
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Cart cart = cartRepo.findByUserUsername(username).get();
		System.out.println(product.getCategory().getCategoryId());
		if (isProductInCart(cart, product)) {
			return ResponseEntity.status(409).build();
		}
		if (productRepo.existsById(product.getProductId())) {
			CartProduct cartProduct = new CartProduct();
			cartProduct.setProduct(product);
			cartProduct.setCart(cart);
			cartProductRepo.save(cartProduct);
			cart.getCartProducts().add(cartProduct);
			cart.updateTotalAmount(product.getPrice() * cartProduct.getQuantity());
			cartRepo.save(cart);

			return ResponseEntity.ok("Product added");
		}
		return ResponseEntity.status(400).build();
	}

	private boolean isProductInCart(Cart cart, Product product) {

		return cart.getCartProducts().stream()
				.anyMatch(cartProduct -> cartProduct.getProduct().getProductId().equals(product.getProductId()));
	}

	@DeleteMapping("/cart")
	public ResponseEntity<Object> deleteCart(@RequestBody Product product) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Cart cart = cartRepo.findByUserUsername(username).get();
		if (isProductInCart(cart, product)) {
			Integer qu = 0;
			for (CartProduct cp : cart.getCartProducts()) {
				if (cp.getProduct().getProductId().equals(product.getProductId())) {
					qu = cp.getQuantity();
					break;
				}
			}
			cart.setTotalAmount(cart.getTotalAmount() - (product.getPrice() * qu));
			cartProductRepo.deleteByCartUserUserIdAndProductProductId(cart.getUser().getUserId(),
					product.getProductId());
			return ResponseEntity.ok("product deleted");
		}
		return ResponseEntity.notFound().build();
	}

}
