package com.fresco.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fresco.ecommerce.DTO.LoginDTO;
import com.fresco.ecommerce.config.JwtUtils;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.ProductRepo;
import com.fresco.ecommerce.repo.UserRepo;
import com.fresco.ecommerce.service.UserAuthService;

@RestController
@RequestMapping("/api/public")
public class PublicController {

	@Autowired
	UserAuthService userAuthService;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/product/search")
	public List<Product> getProducts(@RequestParam(name = "keyword") String keyword) {

		return productRepo.findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(keyword,keyword);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {

		String username = loginDTO.getUsername();
		String password = loginDTO.getPassword();
		String token = null;
		User user = null;
		try {
			userAuthService.authenticate(username, password);
			user = userRepo.findByUsernameAndPassword(username, password);
			if (user != null) {
				token = jwtUtils.generateToken(username);
				return ResponseEntity.ok(token);
			} else {
				return ResponseEntity.status(401).build();
			}

		} catch (Exception e) {
			return ResponseEntity.status(401).build();
		}

	}

}
