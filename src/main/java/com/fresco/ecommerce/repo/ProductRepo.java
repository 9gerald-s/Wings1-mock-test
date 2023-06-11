package com.fresco.ecommerce.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fresco.ecommerce.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>{

	List<Product> findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(String keyword,String keyword1);


	List<Product> findBySellerUsername(String username);
	
	

}
