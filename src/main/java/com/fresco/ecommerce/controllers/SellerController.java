package com.fresco.ecommerce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.Category;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.CartProductRepo;
import com.fresco.ecommerce.repo.CategoryRepo;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {

	@Autowired
	ProductRepo productRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@Autowired
	CartProductRepo cartProductRepo;

	@PostMapping("/product")
	public ResponseEntity<Object> postProduct(@RequestBody Product product) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).get();
		product.setSeller(user);
		productRepo.save(product);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, "/api/auth/seller/product/" + product.getProductId());
		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
//		return ResponseEntity.status(201).build();
	}

	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Product> products = productRepo.findBySellerUsername(username);
		return ResponseEntity.ok(products);
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct(@PathVariable(name = "productId") Integer pid) {
		return ResponseEntity.ok(productRepo.findById(pid));
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct(@RequestBody Product product) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).get();

		if (product.getProductId() != null) {
			Product product2 = productRepo.findById(product.getProductId()).get();
			if (product2.getSeller().getUserId().equals(user.getUserId())) {
				product.setSeller(user);
				return ResponseEntity.ok(productRepo.save(product));
			}
		}

		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable(name = "productId") Integer pid) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username).get();
		if (productRepo.existsById(pid)) {
			Optional<Product> product2 = productRepo.findById(pid);
			if (product2.get().getSeller().getUserId().equals(user.getUserId())) {
				List<CartProduct> cartProducts = cartProductRepo.findByProductProductId(pid);
				cartProducts.forEach(cp -> cartProductRepo.delete(cp));
				productRepo.deleteById(pid);
				return ResponseEntity.ok(product2.get());
			}
		}
		return ResponseEntity.notFound().build();
	}

}
